package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;

public class PowerSourceComponent extends Component {

    private OutputPort northOutput;

    private final Map<String, OutputPort> TAG_TO_PORT;


    public PowerSourceComponent() {
        northOutput = new OutputPort();
        TAG_TO_PORT = java.util.Map.ofEntries(
            Map.entry("north", northOutput)
        );

        northOutput.setActive(true);
    }

    @Override
    public void update() {}


    @Override
    public void connectToPort(Wire wire, String portTag) {
        if(!TAG_TO_PORT.containsKey(portTag)) {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        wire.connectOutputPort(TAG_TO_PORT.get(portTag));
    }

    @Override
    public void disconnectFromPort(Wire wire, String portTag) {
        if(!TAG_TO_PORT.containsKey(portTag)) {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        wire.disconnectOutputPort(TAG_TO_PORT.get(portTag));
    }

    @Override
    public String getAnsiName() {
        return "XOR_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "Power Source";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal to all connected outputs";
    }

}
