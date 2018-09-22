package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

import java.util.List;

public abstract class LogicStateContainer {

    protected Event<LogicStateContainer> onStateChanged = new Event<>();


    /** Run a function while checking if the state changed */
    protected void notifyIfStateChanges(Runnable stateMutator) {
        LogicState prevState = logicState();
        try {
            stateMutator.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(logicState() != prevState) {
            onStateChanged.notifyAll(this);
        }
    }

    public abstract LogicState logicState();

    public final boolean isUndefined() {
        return logicState() == LogicState.UNDEFINED;
    }

    public final boolean isLow() {
        return logicState() == LogicState.LOW;
    }

    public final boolean isHigh() {
        return logicState() == LogicState.HIGH;
    }

    public final Event<LogicStateContainer> onStateChangedEvent() {
        return onStateChanged;
    }
}
