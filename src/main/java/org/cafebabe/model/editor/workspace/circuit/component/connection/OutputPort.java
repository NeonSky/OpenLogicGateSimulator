package org.cafebabe.model.editor.workspace.circuit.component.connection;

import org.cafebabe.model.editor.util.Event;

/**
 * A port that emits a logical state determined by its owner component.
 * Used as outputs for component.
 */
public class OutputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<OutputPort> onWillBeDestroyed = new Event<>();
    private LogicState state;
    private boolean connected;
    private boolean destructionPending;


    public OutputPort() {
        this.state = LogicState.UNDEFINED;
    }

    /* Public */
    public void setState(LogicState state) {
        notifyIfStateChanges(() -> this.state = state);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.onWillBeDestroyed.notifyListeners(this);
        }
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
