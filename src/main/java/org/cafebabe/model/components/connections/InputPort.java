package org.cafebabe.model.components.connections;

public class InputPort extends Port {

    private LogicStateContainer stateSource;


    void setStateSource(LogicStateContainer stateSource) {
        maybeChangeState(() -> {
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
