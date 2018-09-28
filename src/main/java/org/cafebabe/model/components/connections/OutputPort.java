package org.cafebabe.model.components.connections;

import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class OutputPort extends Port {
    private final Event<OutputPort> willBeDestroyed = new Event<>();
    private final EmptyEvent onDestroy = new EmptyEvent();
    private LogicState state;
    private boolean connected;
    private boolean destructionPending = false;


    public OutputPort() {
        state = LogicState.UNDEFINED;
    }

    /* Public */
    public void setState(LogicState state) {
        notifyIfStateChanges(() -> {
            this.state = state;
        });
    }

    public void destroy() {
        if (destructionPending) {
            return;
        }
        destructionPending = true;
        this.onStateChanged.removeListeners();
        willBeDestroyed.notifyListeners(this);
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
        return willBeDestroyed;
    }
}
