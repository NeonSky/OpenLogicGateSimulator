package org.cafebabe.model.circuit.simulation;

import java.util.List;
import java.util.concurrent.Callable;
import org.cafebabe.model.IDynamicComponent;


public class DynamicEvent {
    private final Callable<List<DynamicEvent>> resolution;
    private final long dueTo;
    private final IDynamicComponent source;


    @SuppressWarnings("checkstyle:linelength")
    public DynamicEvent(IDynamicComponent source, long dueIn, Callable<List<DynamicEvent>> resolution) {
        this.source = source;
        this.dueTo = System.currentTimeMillis() + dueIn;
        this.resolution = resolution;
    }


    /* Package-Private */
    long getDueTo() {
        return this.dueTo;
    }

    boolean isDue() {
        return this.dueTo <= System.currentTimeMillis();
    }

    List<DynamicEvent> resolve() {
        List<DynamicEvent> newEvents = null;
        if (!this.source.shouldDie()) {
            try {
                newEvents = this.resolution.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newEvents;
    }
}
