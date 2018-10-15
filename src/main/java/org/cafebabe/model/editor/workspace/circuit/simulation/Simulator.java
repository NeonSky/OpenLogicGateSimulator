package org.cafebabe.model.editor.workspace.circuit.simulation;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.cafebabe.model.editor.util.Event;

/**
 * Simulator simulates the power flow between component and wires.
 * It does this in 2 parts:
 *   1. React to dynamic events from user interactions and dynamic component (e.g. clocks/timers).
 *      This is managed through the priority queue.
 *   2. Resolve chain reaction events from the dynamic event until a final state is calculated.
 *      This is managed through the circuit state breadth-first search.
 */
public class Simulator implements Runnable, IScheduleStateEvents {

    private static final long SIMULATE_INTERVAL = 1000;
    private final Event<SimulationState> onSimulationStateChanged = new Event<>();
    private final Queue<StateEvent> circuitStateBfs;
    private final PriorityQueue<DynamicEvent> upcomingDynamicEvents;

    private ScheduledExecutorService ticker;
    private SimulationState currentSimulationState = SimulationState.STOPPED;
    private long stoppedAt = System.currentTimeMillis();
    private final Set<DynamicEvent> needsPostpone = new HashSet<>();


    public Simulator() {
        this.circuitStateBfs = new ArrayDeque<>();
        this.upcomingDynamicEvents = new PriorityQueue<>(new ResolveAtPriority());
    }


    /* Public */
    public void start() {
        this.needsPostpone.forEach(e -> e.postpone(System.currentTimeMillis() - stoppedAt));

        DaemonThreadFactory factory = new DaemonThreadFactory();
        this.ticker = Executors.newScheduledThreadPool(1, factory);
        this.ticker.scheduleWithFixedDelay(this, 0, SIMULATE_INTERVAL, TimeUnit.MICROSECONDS);
        this.currentSimulationState = SimulationState.STARTED;
        this.onSimulationStateChanged.notifyListeners(this.currentSimulationState);
    }

    public void stop() {
        this.ticker.shutdown();
        this.stoppedAt = System.currentTimeMillis();
        this.needsPostpone.addAll(this.upcomingDynamicEvents);
        this.currentSimulationState = SimulationState.STOPPED;
        this.onSimulationStateChanged.notifyListeners(this.currentSimulationState);
    }

    public void toggleSimulationState() {
        switch (this.currentSimulationState) {
            case STARTED:
                stop();
                break;
            case STOPPED:
                start();
                break;
            default:
                throw new UndefinedSimulationStateException();
        }
    }

    public void registerSimulationStateListener(Consumer<SimulationState> listener) {
        this.onSimulationStateChanged.addListener(listener);
    }

    public void addEvent(DynamicEvent event) {
        this.upcomingDynamicEvents.add(event);
    }

    public void addEvents(List<DynamicEvent> events) {
        this.upcomingDynamicEvents.addAll(events);
    }

    @Override
    public void run() {
        // ScheduledExecutorService is sneaky and doesn't expose exceptions
        // being thrown so we have this try/catch to be notified of any errors.
        try {
            resolveDynamicEvents();
            resolveCircuitState();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /* Private */
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
    public void queueEvent(Event eventToNotify, Object paramToProvide) {
        this.circuitStateBfs.add(new StateEvent(eventToNotify, paramToProvide));
    }
}
