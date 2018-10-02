package org.cafebabe.model.circuit.simulation;

import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.cafebabe.model.IDynamicComponent;

public class Simulator implements Runnable {
    private final PriorityQueue<DynamicEvent> upcomingDynamicEvents;


    public Simulator() {
        this.upcomingDynamicEvents = new PriorityQueue<>(new DueToPriority());

        // call run() every millisecond. Currently needs to be killed somewhere.
        ScheduledExecutorService ticker = Executors.newScheduledThreadPool(1);
        ticker.scheduleAtFixedRate(this, 0, 1, TimeUnit.MILLISECONDS);
    }


    /* Public */
    public void addDynamicComponent(IDynamicComponent comp) {
        addEvents(comp.getInitialDynamicEvents());
    }

    public void removeDynamicComponent(IDynamicComponent comp) {
        // Go through all items in queue and remove
        // Hmmmmm....
        // Add source to dynamic events? OK solution
    }


    /* Private */
    @Override
    public void run() {
        while (!this.upcomingDynamicEvents.isEmpty() && this.upcomingDynamicEvents.peek().isDue()) {
            DynamicEvent top = this.upcomingDynamicEvents.poll();

            try {
                List<DynamicEvent> newEvents = Objects.requireNonNull(top).resolution.call();
                addEvents(newEvents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addEvents(List<DynamicEvent> events) {
        this.upcomingDynamicEvents.addAll(events);
    }

    /*private void removeEvents(List<DynamicEvent> events) {
        this.upcomingDynamicEvents.removeAll(events);
    }*/
}
