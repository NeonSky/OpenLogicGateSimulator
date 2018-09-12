package org.cafebabe.model.components.connections;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Wire {

    private Set<OutputPort> connectedOutputs; // maybe only care about active outputs really and only store them as Object
    private List<InputPort> connectedInputs;


    public Wire() {
        connectedOutputs = new TreeSet<>();
        connectedInputs = new ArrayList<>();
    }


    /** Connects an OutputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectOutputPort(OutputPort output) {
        if(isOutputConnected(output)) {
            throw new RuntimeException("A power source can only be added once.");
        }
        connectedOutputs.add(output);
    }

    /** Disconnects an OutputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectOutputPort(OutputPort output) {
        if(!isOutputConnected(output)) {
            throw new RuntimeException("A power source that isn't connected can't be removed.");
        }
        connectedOutputs.remove(output);
    }

    /** Connects an InputPort if it isn't already connected, otherwise throws a RuntimeException */
    public void connectInputPort(InputPort input) {
        if(isInputConnected(input)) {
            throw new RuntimeException("An InputPort can only be added once.");
        }
        connectedInputs.add(input);
    }

    /** Disconnects an InputPort if it is connected, otherwise throws a RuntimeException */
    public void disconnectInputPort(InputPort input) {
        if(!isInputConnected(input)) {
            throw new RuntimeException("An InputPort that isn't connected can't be removed.");
        }
        connectedInputs.remove(input);
    }

    /** Returns true IFF the wire has a logic state of 1. */
    public boolean isActive() {
        return !connectedOutputs.isEmpty();
    }

    /** Returns true IFF the given OutputPort is connected to this wire. */
    public boolean isOutputConnected(OutputPort output) {
        return connectedOutputs.contains(output);
    }

    /** Returns true IFF the given InputPort is connected to this wire. */
    public boolean isInputConnected(InputPort input) {
        return connectedInputs.contains(input);
    }
}
