package org.cafebabe.model.circuit.simulation;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.cafebabe.util.Event;

/**
 * Simulator simulates the power flow between components and wires.
 * It does this in 2 parts:
 *   1. React to dynamic events from user interactions and dynamic components (e.g. clocks/timers).
 *      This is managed through the priority queue.
 *   2. Resolve chain reaction events from the dynamic event until a final state is calculated.
 *      This is managed through the circuit state breadth-first search.
 */
public class Simulator implements Runnable, IScheduleStateEvents {
    private final Queue<StateEvent> circuitStateBfs;
    private final PriorityQueue<DynamicEvent> upcomingDynamicEvents;
    private final ScheduledExecutorService ticker;
    private static final long SIMULATE_INTERVAL = 1000;


    public Simulator() {
        this.circuitStateBfs = new ArrayDeque<>();
        this.upcomingDynamicEvents = new PriorityQueue<>(new ResolveAtPriority());

        DaemonThreadFactory factory = new DaemonThreadFactory();
        this.ticker = Executors.newScheduledThreadPool(1, factory);
    }


    /* Public */
    public void start() {
        this.ticker.scheduleAtFixedRate(this, 0, this.SIMULATE_INTERVAL, TimeUnit.MICROSECONDS);
    }

    public void stop() {
        this.ticker.shutdown();
    }

    public void addEvents(List<DynamicEvent> events) {
        this.upcomingDynamicEvents.addAll(events);
    }

    @Override
    public void run() {
        resolveDynamicEvents();
        resolveCircuitState();
    }

    private void resolveDynamicEvents() {
        while (!this.upcomingDynamicEvents.isEmpty()
                && this.upcomingDynamicEvents.peek().shouldBeResolved()) {
            DynamicEvent top = this.upcomingDynamicEvents.poll();

            try {
                List<DynamicEvent> newEvents = Objects.requireNonNull(top).resolve();
                if (newEvents != null) {
                    addEvents(newEvents);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resolveCircuitState() {
        while (!this.circuitStateBfs.isEmpty()) {
            StateEvent event = this.circuitStateBfs.poll();
            event.eventToNotify.notifyListeners(event.paramToProvide);
        }
    }

    @Override
    public void queueEvent(StateEvent event) {
        this.circuitStateBfs.add(event);
    }

    @Override
    public void queueEvent(Event eventToNotify, Object paramToProvide) {
        queueEvent(new StateEvent(eventToNotify, paramToProvide));
    }
}
