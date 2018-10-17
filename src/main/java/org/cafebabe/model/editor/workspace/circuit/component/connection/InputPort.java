package org.cafebabe.model.editor.workspace.circuit.component.connection;

import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.InvalidStateSourceException;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.StateSourceAlreadySetException;
import org.cafebabe.model.util.Event;

/**
 * A port that reflects the logical state of its
 * connected wire (if any) through a read-only interface.
 * Used as inputs to component.
 */
public class InputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<InputPort> onWillBeDestroyed = new Event<>();
    private LogicStateContainer stateSource;
    private boolean destructionPending;

    /* Public */
    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.onWillBeDestroyed.notifyListeners(this);
        }
    }

    @Override
    public LogicState logicState() {
        if (this.stateSource == null) {
            return LogicState.UNDEFINED;
        }
        return this.stateSource.logicState();
    }

    @Override
    public boolean isConnected() {
        return this.stateSource != null;
    }

    public Event<InputPort> onWillBeDestroyed() {
        return this.onWillBeDestroyed;
    }

    /* Package-Private */
    void setStateSource(LogicStateContainer stateSource) {
        notifyIfStateChanges(() -> {
            if (this.stateSource != null) {
                throw new StateSourceAlreadySetException("Can't connect multiple "
                        + "wires to an input");
            }

            if (stateSource instanceof Port) {
                throw new InvalidStateSourceException("Can't directly connect an "
                        + "input port with another port");
            }

            this.stateSource = stateSource;
            stateSource.onStateChanged.addListener((s) -> {
                onStateChanged.notifyListeners(this);
            });
        });
    }

    void removeStateSource() {
        this.stateSource = null;
    }
}
