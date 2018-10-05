package org.cafebabe.model.components.connections;

import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

/**
 * A port that represents the input value of a component.
 * Can be powered by a connected wire.
 */
public class InputPort extends Port {
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Event<InputPort> onWillBeDestroyed = new Event<>();
    private final EmptyEvent onDestroy = new EmptyEvent();
    private LogicStateContainer stateSource;
    private boolean destructionPending;

    /* Public */
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
                throw new RuntimeException("Can't directly connect an "
                        + "input port with another port");
            }
            if (this.stateSource != null) {
                throw new RuntimeException("Can't connect multiple wires to an input");
            }
            this.stateSource = stateSource;

            stateSource.onStateChanged.addListener((s) -> onStateChanged.notifyListeners(this));
        });
    }

    void removeStateSource() {
        this.stateSource = null;
    }
}
