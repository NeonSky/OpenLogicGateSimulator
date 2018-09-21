package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Wire extends LogicStateContainer {

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
        maybeChangeState(() -> {
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
        maybeChangeState(() -> {
            if (updatedPort.isHigh()) {
                powerSources.add(updatedPort);
            } else {
                powerSources.remove(updatedPort);
            }

            if (updatedPort.isLow()) {
                gndSources.add(updatedPort);
            } else {
                gndSources.remove(updatedPort);
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
        maybeChangeState(() -> {
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
        maybeChangeState(() -> {
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
        if(gndSources.isEmpty() && powerSources.isEmpty()) return LogicState.UNDEFINED;
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
