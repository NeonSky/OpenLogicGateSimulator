package org.cafebabe.model.editor.workspace.circuit.component.flipflop;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A latch that stores data D if e is active, otherwise remains.
 */
public class DFlipFlopComponent extends Component {

    private final InputPort dataInput;
    private final InputPort enableInput;
    private final OutputPort output;
    private final OutputPort inverseOutput;

    @ComponentConstructor
    public DFlipFlopComponent() {
        super("D_FLIP_FLOP", "D Flip Flop", "Stores data D if e is active, otherwise remains.");
        this.dataInput = new InputPort();
        this.enableInput = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("dataInput", this.dataInput),
                Map.entry("enableInput", this.enableInput)
        );

        this.output = new OutputPort();
        this.inverseOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output),
                Map.entry("inverseOutput", this.inverseOutput)
        );

        this.dataInput.onStateChangedEvent().addListener(p -> updateOutputs());
        this.enableInput.onStateChangedEvent().addListener(p -> updateOutputs());
    }

    /* Protected */

    @Override
    protected void updateOutputs() {
        boolean currentState = this.output.isHigh();
        boolean d = this.dataInput.isHigh();
        boolean e = this.enableInput.isHigh();
        boolean newState = e ? d : currentState;
        this.setOutputState(this.output, newState, Arrays.asList(this.dataInput, this.enableInput));
        this.setOutputState(this.inverseOutput, !newState,
                Arrays.asList(this.dataInput, this.enableInput));
    }

}
