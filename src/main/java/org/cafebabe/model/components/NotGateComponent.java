package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;


public class NotGateComponent extends Component {

    private InputPort input;
    private OutputPort output;

    private final Map<String, InputPort> TAG_TO_INPUT;
    private final Map<String, OutputPort> TAG_TO_OUTPUT;

    @ComponentConstructor
    public NotGateComponent() {
        input = new InputPort();
        TAG_TO_INPUT = Map.ofEntries(
                Map.entry("input", input)
        );

        output = new OutputPort();
        TAG_TO_OUTPUT = Map.ofEntries(
                Map.entry("output", output)
        );

        input.onStateChangedEvent().addListener(p -> update());
    }

    private void update() {
        output.setActive(!input.isActive());
    }


    @Override
    public void connectToPort(Wire wire, String portTag) {
        if(TAG_TO_INPUT.containsKey(portTag)) {
            wire.connectInputPort(TAG_TO_INPUT.get(portTag));
        }
        else if(TAG_TO_OUTPUT.containsKey(portTag)) {
            wire.connectOutputPort(TAG_TO_OUTPUT.get(portTag));
        }
        else {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        update();
    }

    @Override
    public void disconnectFromPort(Wire wire, String portTag) {
        if(!TAG_TO_OUTPUT.containsKey(portTag)) {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        wire.disconnectOutputPort(TAG_TO_OUTPUT.get(portTag));
        update();
    }

    @Override
    public String getAnsiName() {
        return "NOT_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NOT-Gate";
    }

    @Override
    public String getDescription() {
        return "Emits the opposite signal of the input";
    }

}
