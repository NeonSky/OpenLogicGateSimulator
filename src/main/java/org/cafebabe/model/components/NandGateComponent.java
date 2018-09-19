package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;


public class NandGateComponent extends Component {

    private InputPort input1, input2;
    private OutputPort output;

    @ComponentConstructor
    public NandGateComponent() {
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

    @Override
    protected void update() {
        output.setActive(!(input1.isActive() && input2.isActive()));
    }

    @Override
    public String getAnsiName() {
        return "NAND_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NAND-Gate";
    }

    @Override
    public String getDescription() {
        return "Emits no signal if both inputs are active";
    }

}
