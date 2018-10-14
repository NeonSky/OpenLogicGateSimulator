package org.cafebabe.model.editor.workspace.circuit.component.flipflop;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A latch that toggles output if T is active on clock.
 */
public class TFlipFlopComponent extends Component {

    @SuppressWarnings("CheckStyle")
    private final InputPort toggleInput;
    private final InputPort clkInput;
    private final OutputPort output;
    private final OutputPort inverseOutput;
    private boolean clkWasLow;

    @ComponentConstructor
    public TFlipFlopComponent() {
        super("T_FLIP_FLOP", "T Flip Flop", "Toggle output if T is active on clock");
        this.toggleInput = new InputPort();
        this.clkInput = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("toggleInput", this.toggleInput),
                Map.entry("clkInput", this.clkInput)
        );

        this.output = new OutputPort();
        this.inverseOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output),
                Map.entry("inverseOutput", this.inverseOutput)
        );

        this.toggleInput.onStateChangedEvent().addListener(p -> updateOutputs());
        this.clkInput.onStateChangedEvent().addListener(p -> updateOutputs());
    }

    /* Public */

    @Override
    protected void updateOutputs() {
        boolean currentState = this.output.isHigh();
        boolean t = this.toggleInput.isHigh();
        boolean clk = this.clkInput.isHigh();
        boolean newState = (this.clkWasLow && clk && t) != currentState;
        this.setOutputState(this.output, newState, Arrays.asList(this.toggleInput, this.clkInput));
        this.setOutputState(this.inverseOutput, !newState,
                Arrays.asList(this.toggleInput, this.clkInput));
        this.clkWasLow = !clk;
    }

}
