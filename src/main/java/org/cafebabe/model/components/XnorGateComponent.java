package org.cafebabe.model.components;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;


public class XnorGateComponent extends Component {

    private final InputPort input1;
    private final InputPort input2;
    private final OutputPort output;

    @ComponentConstructor
    public XnorGateComponent() {
        input1 = new InputPort();
        input2 = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input1", input1),
                Map.entry("input2", input2)
        );

        output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", output)
        );

        input1.onStateChangedEvent().addListener(p -> update());
        input2.onStateChangedEvent().addListener(p -> update());
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "XNOR_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "XNOR Gate";
    }

    @Override
    public String getDescription() {
        return "Emits no signal if exactly one input is active";
    }

    @Override
    protected void update() {
        setOutputState(
                output,
                (input1.isHigh() && input2.isHigh()) || (input1.isLow() && input2.isLow()),
                Arrays.asList(input1, input2));
    }

}
