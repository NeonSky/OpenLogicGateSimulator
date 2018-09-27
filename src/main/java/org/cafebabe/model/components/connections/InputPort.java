package org.cafebabe.model.components.connections;

import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class InputPort extends Port {
    private final Event<InputPort> willBeDestroyed = new Event<>();
    private final EmptyEvent onDestroy = new EmptyEvent();
    private LogicStateContainer stateSource;
    private boolean destructionPending = false;

    /* Public */
    public void destroy() {
        if (destructionPending) return;
        destructionPending = true;
        onStateChanged.removeListeners();
        willBeDestroyed.notifyListeners(this);
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    @Override
    public EmptyEvent getOnDestroy() {
        return onDestroy;
    }

    @Override
    public LogicState logicState() {
        if (stateSource == null) return LogicState.UNDEFINED;
        return stateSource.logicState();
    }

    @Override
    public boolean isConnected() {
        return stateSource != null;
    }

    public Event<InputPort> onWillBeDestroyed() {
        return willBeDestroyed;
    }

    /* Package-Private */
    void setStateSource(LogicStateContainer stateSource) {
        notifyIfStateChanges(() -> {
            if (stateSource instanceof Port) {
                throw new RuntimeException("Can't directly connect an input port with another port");
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
