package org.cafebabe.model.editor.workspace.circuit.component.flipflop;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A latch that sets a positive value on S=1 and resets to 0 on R=1.
 */
public class SrFlipFlopComponent extends Component {

    private final InputPort setInput;
    private final InputPort resetInput;
    private final OutputPort output;
    private final OutputPort inverseOutput;

    @ComponentConstructor
    public SrFlipFlopComponent() {
        super("FLIPFLOP_SR", "SR Flip Flop",
                "A latch that sets a positive value on S=1 and resets to 0 on R=1.");
        this.setInput = new InputPort();
        this.resetInput = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("setInput", this.setInput),
                Map.entry("resetInput", this.resetInput)
        );

        this.output = new OutputPort();
        this.inverseOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output),
                Map.entry("inverseOutput", this.inverseOutput)
        );

        this.setInput.getOnStateChanged().addListener(p -> updateOutputs());
        this.resetInput.getOnStateChanged().addListener(p -> updateOutputs());
    }

    /* Protected */
    @Override
    protected void updateOutputs() {
        boolean currentState = this.output.isHigh();
        boolean s = this.setInput.isHigh();
        boolean r = this.resetInput.isHigh();
        boolean newState = true;
        if (s) {
            if (r) {
                this.output.setLogicState(LogicState.UNDEFINED);
                this.inverseOutput.setLogicState(LogicState.UNDEFINED);
                return;
            }
        } else {
            newState = !r && currentState;
        }
        this.setOutputState(this.output, newState, Arrays.asList(this.setInput, this.resetInput));

        this.setOutputState(this.inverseOutput, !newState,
                Arrays.asList(this.setInput, this.resetInput));
    }

}
