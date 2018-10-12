package org.cafebabe.model.components.connections;

import org.cafebabe.model.components.connections.exceptions.InvalidStateSourceException;
import org.cafebabe.model.components.connections.exceptions.StateSourceAlreadySetException;
import org.cafebabe.model.util.Event;

/**
 * A port that reflects the logical state of its
 * connected wire (if any) through a read-only interface.
 * Used as inputs to components.
 */
public class InputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<InputPort> onWillBeDestroyed = new Event<>();
    private LogicStateContainer stateSource;

    /* Public */
    @Override
    public void destroy() {
        super.destroy();
        this.onWillBeDestroyed.notifyListeners(this);
        this.onWillBeDestroyed.removeListeners();
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
            if (stateSource instanceof Port) {
                throw new InvalidStateSourceException("Can't directly connect an "
                        + "input port with another port");
            }
            if (this.stateSource != null) {
                throw new StateSourceAlreadySetException("Can't connect multiple "
                        + "wires to an input");
            }
            this.stateSource = stateSource;

            stateSource.onStateChanged.addListener((s) -> onStateChanged.notifyListeners(this));
        });
    }

    void removeStateSource() {
        this.stateSource = null;
    }
}
