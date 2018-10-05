package org.cafebabe.model.components.connections;

import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

/**
 * A port that represents the output value of a component.
 * Can potentially power a wire.
 */
public class OutputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<OutputPort> onWillBeDestroyed = new Event<>();
    private final EmptyEvent onDestroy = new EmptyEvent();
    private LogicState state;
    private boolean connected;
    private boolean destructionPending;


    public OutputPort() {
        this.state = LogicState.UNDEFINED;
    }

    /* Public */
    public void setState(LogicState state) {
        notifyIfStateChanges(() -> {
            this.state = state;
        });
    }

    @Override
    public void destroy() {
        if (this.destructionPending) {
            return;
        }
        this.destructionPending = true;
        this.onStateChanged.removeListeners();
        this.onWillBeDestroyed.notifyListeners(this);
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    @Override
    public EmptyEvent getOnDestroy() {
        return this.onDestroy;
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
        return this.state;
    }

    public Event<OutputPort> onWillBeDestroyed() {
        return this.onWillBeDestroyed;
    }
}
