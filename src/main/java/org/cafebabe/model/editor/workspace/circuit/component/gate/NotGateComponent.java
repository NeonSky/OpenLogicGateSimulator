package org.cafebabe.model.editor.workspace.circuit.component.gate;

import java.util.Collections;
import java.util.Map;

import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/** A component that outputs the logical NOT operation of its input. */
public class NotGateComponent extends Component {

    private final InputPort input;
    private final OutputPort output;

    @ComponentConstructor
    public NotGateComponent() {
        super("GATE_Not", "NOT Gate", "Emits the opposite signal of the input");
        this.input = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input", this.input)
        );

        this.output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output)
        );

        this.input.getOnStateChanged().addListener(p -> updateOutputs());
    }

    @Override
    protected void updateOutputs() {
        setOutputState(this.output, !this.input.isHigh(), Collections.singletonList(this.input));
    }

}
