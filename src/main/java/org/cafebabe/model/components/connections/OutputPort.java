package org.cafebabe.model.components.connections;

public class OutputPort extends Port {

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
