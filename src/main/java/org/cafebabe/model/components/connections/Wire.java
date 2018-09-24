package org.cafebabe.model.components.connections;

import org.cafebabe.model.circuit.IBelongToCircuit;
import org.cafebabe.util.Event;
import java.util.*;

public class Wire extends LogicStateContainer implements IBelongToCircuit {

    private Set<InputPort> connectedInputs;
    private Set<OutputPort> connectedOutputs;
    private Set<LogicStateContainer> powerSources;
    private Set<LogicStateContainer> gndSources;


    public Wire() {
        onStateChanged = new Event<>();
        connectedInputs = new HashSet<>();
        connectedOutputs = new HashSet<>();
        powerSources = new HashSet<>();
        gndSources = new HashSet<>();
    }

    public void disconnectAll() {
        notifyIfStateChanges(() -> {
            for(InputPort inport : this.connectedInputs) {
                disconnectInputPort(inport);
            }
            for(OutputPort outport : this.connectedOutputs) {
                disconnectOutputPort(outport);
            }
        });
    }

    /** Updates the wire's logical value based on updated output value */
    private void onConnectedOutputStateChanged(LogicStateContainer updatedPort) {
        if(!connectedOutputs.contains(updatedPort)) {
            throw new RuntimeException("Updated port isn't connected!");
        }
        notifyIfStateChanges(() -> {
            switch(updatedPort.logicState()) {
                case LOW:
                    gndSources.add(updatedPort);
                    powerSources.remove(updatedPort);
                    break;
                case HIGH:
                    gndSources.remove(updatedPort);
                    powerSources.add(updatedPort);
                    break;
                case UNDEFINED:
                    gndSources.remove(updatedPort);
                    powerSources.remove(updatedPort);
                    break;
            }
        });
    }

    /** Connects an InputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectInputPort(InputPort input) {
        if (isInputConnected(input)) {
            throw new RuntimeException("An InputPort can only be added once.");
        }
        connectedInputs.add(input);
        input.setStateSource(this);
    }

    /** Disconnects an InputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectInputPort(InputPort input) {
        if(!isInputConnected(input)) {
            throw new RuntimeException("An InputPort that isn't connected can't be removed.");
        }
        connectedInputs.remove(input);
        input.setStateSource(null);
    }

    /** Connects an OutputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if(isOutputConnected(output)) {
                throw new RuntimeException("An OutputPort can only be added once.");
            }

            output.setConnected(true);
            connectedOutputs.add(output);
            if(output.isHigh()) powerSources.add(output);
            if(output.isLow()) gndSources.add(output);

            output.onStateChangedEvent().addListener(this::onConnectedOutputStateChanged);
        });
    }

    /** Disconnects an OutputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if(!isOutputConnected(output)) {
                throw new RuntimeException("An OutputPort that isn't connected can't be removed.");
            }

            output.setConnected(false);
            connectedOutputs.remove(output);
            powerSources.remove(output);
            gndSources.remove(output);

            output.onStateChangedEvent().removeListener(this::onConnectedOutputStateChanged);
        });
    }

    @Override
    public LogicState logicState() {
        if(powerSources.isEmpty() && gndSources.isEmpty()) return LogicState.UNDEFINED;
        if(powerSources.isEmpty()) return LogicState.LOW;
        return LogicState.HIGH;
    }

    /** Returns true IFF the given InputPort is connected to this wire. */
    private boolean isInputConnected(InputPort input) {
        return connectedInputs.contains(input);
    }

    public boolean isAnyInputConnected() {
        return !connectedInputs.isEmpty();
    }

    /** Returns true IFF the given OutputPort is connected to this wire. */
    private boolean isOutputConnected(OutputPort output) {
        return connectedOutputs.contains(output);
    }

    public boolean isAnyOutputConnected() {
        return !connectedOutputs.isEmpty();
    }

    public IConnectionState getConnectionState() {
        Wire wire = this;
        return new IConnectionState() {
            @Override
            public boolean isConnecting() {
                return this.isInportConnected() || this.isOutportConnected();
            }

            @Override
            public boolean isInportConnected() {
                return wire.isAnyInputConnected();
            }

            @Override
            public boolean isOutportConnected() {
                return wire.isAnyOutputConnected();
            }
        };
    }
}
