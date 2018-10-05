package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

/**
 * Contains a logic state.
 * This logic state may change and may affect others' logic state.
 */
public abstract class LogicStateContainer {

    Event<LogicStateContainer> onStateChanged = new Event<>();

    /* Public */
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
        return this.onStateChanged;
    }

    /* Package-Private */

    /**
     * Run a function while checking if the state changed.
     */
    void notifyIfStateChanges(Runnable stateMutator) {
        LogicState prevState = logicState();
        try {
            stateMutator.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logicState() != prevState) {
            this.onStateChanged.notifyListeners(this);
        }
    }

    /* Protected */
    protected abstract LogicState logicState();
}
