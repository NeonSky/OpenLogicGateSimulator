package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

/**
 * A port that emits a logical state determined by its owner component.
 * Used as outputs for components.
 */
public class OutputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<OutputPort> onWillBeDestroyed = new Event<>();
    private LogicState state;
    private boolean connected;


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
        super.destroy();
        this.onWillBeDestroyed.notifyListeners(this);
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
