package org.cafebabe.model.editor.workspace.circuit.simulation;

import java.util.List;
import java.util.concurrent.Callable;
import org.cafebabe.model.editor.workspace.circuit.component.IDynamicComponent;


/**
 * Represents an event that will be resolved at a specific time.
 * Components can e.g. use this to set an output high after a set amount of time.
 */
public class DynamicEvent {
    private final Callable<List<DynamicEvent>> dueFunc;
    private long resolveAt;
    private final IDynamicComponent source;


    @SuppressWarnings("checkstyle:linelength")
    public DynamicEvent(IDynamicComponent source, long resolveIn, Callable<List<DynamicEvent>> dueFunc) {
        this.source = source;
        this.resolveAt = System.currentTimeMillis() + resolveIn;
        this.dueFunc = dueFunc;
    }


    /* Package-Private */
    long getResolveAt() {
        return this.resolveAt;
    }

    boolean shouldBeResolved() {
        return this.resolveAt <= System.currentTimeMillis();
    }

    void postpone(long amount) {
        this.resolveAt += amount;
    }

    List<DynamicEvent> resolve() {
        List<DynamicEvent> newEvents = null;
        if (!this.source.shouldDie()) {
            try {
                newEvents = this.dueFunc.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newEvents;
    }
}
