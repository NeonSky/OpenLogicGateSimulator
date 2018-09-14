package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;


public class AndGateComponent extends Component {

    private InputPort input1, input2;
    private OutputPort output;

    private final Map<String, InputPort> TAG_TO_INPUT;
    private final Map<String, OutputPort> TAG_TO_OUTPUT;

    @ComponentConstructor
    public AndGateComponent() {
        input1 = new InputPort();
        input2 = new InputPort();
        TAG_TO_INPUT = Map.ofEntries(
            Map.entry("input1", input1),
            Map.entry("input2", input2)
        );

        output = new OutputPort();
        TAG_TO_OUTPUT = Map.ofEntries(
            Map.entry("output", output)
        );

        input1.onStateChangedEvent().addListener(p -> update());
        input2.onStateChangedEvent().addListener(p -> update());
    }

    private void update() {
        output.setActive(input1.isActive() && input2.isActive());
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
        return "AND_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "AND-Gate";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal if both inputs are active";
    }

}
