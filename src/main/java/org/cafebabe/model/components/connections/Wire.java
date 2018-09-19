package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;
import java.util.*;

public class Wire {

    private Event<Wire> onStateChanged;
    private Set<InputPort> connectedInputs;
    private Set<OutputPort> connectedOutputs;
    private Set<OutputPort> powerSources;


    public Wire() {
        onStateChanged = new Event<>();
        connectedInputs = new HashSet<>();
        connectedOutputs = new HashSet<>();
        powerSources = new HashSet<>();
    }

    /** Updates the wire's logical value based on updated output value */
    private void onConnectedOutputStateChanged(OutputPort updatedPort) {
        boolean wasActive = isActive();
        if(updatedPort.isActive()) {
            powerSources.add(updatedPort);
        } else {
            powerSources.remove(updatedPort);
        }
        if(isActive() != wasActive) {
            onStateChanged.notifyAll(this);
        }
    }

    /** Connects an InputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectInputPort(InputPort input) {
        if(isInputConnected(input)) {
            throw new RuntimeException("An InputPort can only be added once.");
        }
        connectedInputs.add(input);
        input.connectWire(this);
    }

    /** Disconnects an InputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectInputPort(InputPort input) {
        if(!isInputConnected(input)) {
            throw new RuntimeException("An InputPort that isn't connected can't be removed.");
        }
        connectedInputs.remove(input);
        input.disconnectWire(this);
    }

    /** Connects an OutputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectOutputPort(OutputPort output) {
        if(isOutputConnected(output)) {
            throw new RuntimeException("An OutputPort can only be added once.");
        }
        output.setConnected(true);
        connectedOutputs.add(output);
        if(output.isActive()) {
            powerSources.add(output);
        }
        output.onStateChangedEvent().addListener(this::onConnectedOutputStateChanged);
    }

    /** Disconnects an OutputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectOutputPort(OutputPort output) {
        if(!isOutputConnected(output)) {
            throw new RuntimeException("An OutputPort that isn't connected can't be removed.");
        }
        output.setConnected(false);
        connectedOutputs.remove(output);
        powerSources.remove(output);
        output.onStateChangedEvent().removeListener(this::onConnectedOutputStateChanged);
    }

    /** Returns true IFF the given InputPort is connected to this wire. */
    public boolean isInputConnected(InputPort input) {
        return connectedInputs.contains(input);
    }

    public boolean isAnyInputConnected() {
        return !connectedInputs.isEmpty();
    }

    /** Returns true IFF the given OutputPort is connected to this wire. */
    public boolean isOutputConnected(OutputPort output) {
        return connectedOutputs.contains(output);
    }

    public boolean isAnyOutputConnected() {
        return !connectedOutputs.isEmpty();
    }

    /** Returns true IFF the wire has a logic state of 1. */
    public boolean isActive() {
        return !powerSources.isEmpty();
    }

    /** Is notified with the new logical value of the output whenever it is changed */
    public Event<Wire> onStateChangedEvent() {
        return onStateChanged;
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
