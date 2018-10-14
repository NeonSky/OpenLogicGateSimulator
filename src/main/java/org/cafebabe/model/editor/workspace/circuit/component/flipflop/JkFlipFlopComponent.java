package org.cafebabe.model.editor.workspace.circuit.component.flipflop;

import java.util.Arrays;
import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A latch that works like SR, but that flips its output on both inputs high.
 */
public class JkFlipFlopComponent extends Component {

    private final InputPort jackInput;
    private final InputPort kilbyInput;
    private final InputPort clkInput;
    private final OutputPort output;
    private final OutputPort inverseOutput;

    @ComponentConstructor
    public JkFlipFlopComponent() {
        this.jackInput = new InputPort();
        this.kilbyInput = new InputPort();
        this.clkInput = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("jackInput", this.jackInput),
                Map.entry("kilbyInput", this.kilbyInput),
                Map.entry("clkInput", this.clkInput)
        );

        this.output = new OutputPort();
        this.inverseOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output),
                Map.entry("inverseOutput", this.inverseOutput)
        );

        this.jackInput.onStateChangedEvent().addListener(p -> updateOutputs());
        this.kilbyInput.onStateChangedEvent().addListener(p -> updateOutputs());
        this.clkInput.onStateChangedEvent().addListener(p -> updateOutputs());
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "JK_FLIP_FLOP_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "JK Flip Flop";
    }

    @Override
    public String getDescription() {
        return "Like SR but 1, 1 flips";
    }

    @Override
    protected void updateOutputs() {
        if (!this.clkInput.isHigh()) {
            return;
        }
        boolean currentState = this.output.isHigh();
        boolean j = this.jackInput.isHigh();
        boolean k = this.kilbyInput.isHigh();
        boolean newState;
        if (j) {
            newState = !k || !currentState;
        } else {
            newState = !k && currentState;
        }
        this.setOutputState(this.output, newState, Arrays.asList(this.jackInput, this.kilbyInput));
        this.setOutputState(this.inverseOutput, !newState,
                Arrays.asList(this.jackInput, this.kilbyInput));
    }

}
