package org.cafebabe.model.circuit.simulation;

import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Simulator implements Runnable {
    private final ScheduledExecutorService ticker;
    private final PriorityQueue<DynamicEvent> upcomingDynamicEvents;
    private static final long SIMULATE_INTERVAL = 1000;


    public Simulator() {
        this.upcomingDynamicEvents = new PriorityQueue<>(new DueToPriority());
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

    /* Private */
    @Override
    public void run() {
        while (!this.upcomingDynamicEvents.isEmpty() && this.upcomingDynamicEvents.peek().isDue()) {
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
}
