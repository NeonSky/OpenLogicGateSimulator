package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

public class InputPort extends Port {

    private final Event<InputPort> willBeDestroyed = new Event<>();
    private LogicStateContainer stateSource;


    void setStateSource(LogicStateContainer stateSource) {
        notifyIfStateChanges(() -> {
            if(stateSource instanceof Port) {
                throw new RuntimeException("Can't directly connect an input port with another port");
            }
            if(this.stateSource != null && stateSource != null) {
                throw new RuntimeException("Can't connect multiple wires to an input");
            }
            this.stateSource = stateSource;

            if(this.stateSource != null) {
                stateSource.onStateChanged.addListener((s) -> onStateChanged.notifyAll(this));
            }
        });
    }

    public Event<InputPort> onWillBeDestroyed() {
        return willBeDestroyed;
    }

    public void destroy() {
        willBeDestroyed.notifyAll(this);
    }

    @Override
    public LogicState logicState() {
        if(stateSource == null) return LogicState.UNDEFINED;
        return stateSource.logicState();
    }

    @Override
    public boolean isConnected() {
        return stateSource != null;
    }

}
