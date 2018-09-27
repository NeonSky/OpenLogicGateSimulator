package org.cafebabe.model.components;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;


public class XorGateComponent extends Component {

    private final InputPort input1;
    private final InputPort input2;
    private final OutputPort output;

    @ComponentConstructor
    public XorGateComponent() {
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

    /* Public */
    @Override
    public String getAnsiName() {
        return "XOR_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "XOR Gate";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal if exactly one input is active";
    }

    @Override
    protected void update() {
        setOutputState(
                output,
                (input1.isHigh() && input2.isLow()) || (input1.isLow() && input2.isHigh()),
                Arrays.asList(input1, input2));
    }

}
