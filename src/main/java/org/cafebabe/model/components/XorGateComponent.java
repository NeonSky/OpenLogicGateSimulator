package org.cafebabe.model.components;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;

/** A component that outputs the logical XOR operation of its two inputs. */
public class XorGateComponent extends Component {

    private final InputPort input1;
    private final InputPort input2;
    private final OutputPort output;

    @ComponentConstructor
    public XorGateComponent() {
        this.input1 = new InputPort();
        this.input2 = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input1", this.input1),
                Map.entry("input2", this.input2)
        );

        this.output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output)
        );

        this.input1.onStateChangedEvent().addListener(p -> update());
        this.input2.onStateChangedEvent().addListener(p -> update());
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
    @SuppressWarnings("PMD.UselessParentheses")
    protected void update() {
        setOutputState(this.output,
                (this.input1.isHigh() && this.input2.isLow())
                || (this.input1.isLow() && this.input2.isHigh()),
                Arrays.asList(this.input1, this.input2));
    }

}
