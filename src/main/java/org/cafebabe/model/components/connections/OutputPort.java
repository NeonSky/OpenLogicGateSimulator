package org.cafebabe.model.components.connections;

import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class OutputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<OutputPort> onWillBeDestroyed = new Event<>();
    private final EmptyEvent onDestroy = new EmptyEvent();
    private LogicState state;
    private boolean connected;
    private boolean destructionPending;


    public OutputPort() {
        state = LogicState.UNDEFINED;
    }

    /* Public */
    public void setState(LogicState state) {
        notifyIfStateChanges(() -> {
            this.state = state;
        });
    }

    @Override
    public void destroy() {
        if (destructionPending) {
            return;
        }
        destructionPending = true;
        this.onStateChanged.removeListeners();
        onWillBeDestroyed.notifyListeners(this);
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    @Override
    public EmptyEvent getOnDestroy() {
        return onDestroy;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public LogicState logicState() {
        return state;
    }

    public Event<OutputPort> onWillBeDestroyed() {
        return onWillBeDestroyed;
    }
}
