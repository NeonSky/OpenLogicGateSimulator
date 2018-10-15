package org.cafebabe.model.editor.workspace.circuit.component.gate;

import java.util.Arrays;
import java.util.Map;

import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/** A component that outputs the logical AND operation of its two inputs. */
public class AndGateComponent extends Component {

    private final InputPort input1;
    private final InputPort input2;
    private final OutputPort output;

    @ComponentConstructor
    public AndGateComponent() {
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
        return "AND_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "AND Gate";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal if both inputs are active";
    }

    @Override
    protected void updateOutputs() {
        setOutputState(this.output, this.input1.isHigh() && this.input2.isHigh(),
                Arrays.asList(this.input1, this.input2));
    }

}