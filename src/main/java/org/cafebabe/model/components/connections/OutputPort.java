package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

public class OutputPort extends Port {

    private final Event<OutputPort> willBeDestroyed = new Event<>();
    private LogicState state;
    private boolean connected;


    public OutputPort() {
        state = LogicState.UNDEFINED;
    }

    public void setState(LogicState state) {
        notifyIfStateChanges(() -> {
            this.state = state;
        });
    }


    public Event<OutputPort> onWillBeDestroyed() {
        return willBeDestroyed;
    }

    public void destroy() {
        willBeDestroyed.notifyListeners(this);
    }

    void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public LogicState logicState() {
        return state;
    }
}
