package org.cafebabe.model.editor.workspace.circuit.component.arithmetic;

import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/** A component that counts in binary from 0 to 15 on high flank and loops around. */
public class CounterComponent extends Component {

    private final InputPort clkInput;
    private final OutputPort bit0output;
    private final OutputPort bit1output;
    private final OutputPort bit2output;
    private final OutputPort bit3output;
    private int value;

    @ComponentConstructor
    public CounterComponent() {
        this.clkInput = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("clkInput", this.clkInput)
        );

        this.bit0output = new OutputPort();
        this.bit1output = new OutputPort();
        this.bit2output = new OutputPort();
        this.bit3output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("bit0output", this.bit0output),
                Map.entry("bit1output", this.bit1output),
                Map.entry("bit2output", this.bit2output),
                Map.entry("bit3output", this.bit3output)
        );

        this.clkInput.onStateChangedEvent().addListener(p -> updateOutputs());
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "COUNTER_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "Counter";
    }

    @Override
    public String getDescription() {
        return "Counts in binary from 0 to 15";
    }

    @Override
    protected void updateOutputs() {
        if (this.clkInput.isHigh()) {
            this.value++;
            this.bit0output.setState((this.value & 0b1) > 0 ? LogicState.HIGH : LogicState.LOW);
            this.bit1output.setState((this.value & 0b01) > 0 ? LogicState.HIGH : LogicState.LOW);
            this.bit2output.setState((this.value & 0b001) > 0 ? LogicState.HIGH : LogicState.LOW);
            this.bit3output.setState((this.value & 0b0001) > 0 ? LogicState.HIGH : LogicState.LOW);
        }
    }

}
