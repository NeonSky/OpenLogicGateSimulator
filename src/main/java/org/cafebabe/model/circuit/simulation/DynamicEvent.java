package org.cafebabe.model.circuit.simulation;

import java.util.List;
import java.util.concurrent.Callable;

public class DynamicEvent {
    final long dueTo;
    final Callable<List<DynamicEvent>> resolution;


    public DynamicEvent(long dueIn, Callable<List<DynamicEvent>> resolution) {
        this.dueTo = System.currentTimeMillis() + dueIn;
        this.resolution = resolution;
    }


    /* Package-Private */
    boolean isDue() {
        return this.dueTo <= System.currentTimeMillis();
    }
}
