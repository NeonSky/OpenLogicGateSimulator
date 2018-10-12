package org.cafebabe.model.components;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;

/** A component that outputs the logical NOR operation of its two inputs. */
public class NorGateComponent extends Component {

    private final InputPort input1;
    private final InputPort input2;
    private final OutputPort output;

    @ComponentConstructor
    public NorGateComponent() {
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

        this.input1.onStateChangedEvent().addListener(p -> updateOutputs());
        this.input2.onStateChangedEvent().addListener(p -> updateOutputs());
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "NOR_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NOR Gate";
    }

    @Override
    public String getDescription() {
        return "Emits no signal if any inputs are active";
    }

    @Override
    protected void updateOutputs() {
        setOutputState(this.output, !(this.input1.isHigh() || this.input2.isHigh()),
                Arrays.asList(this.input1, this.input2));
    }

}
