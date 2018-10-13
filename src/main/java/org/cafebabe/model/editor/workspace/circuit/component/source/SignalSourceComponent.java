package org.cafebabe.model.editor.workspace.circuit.component.source;

import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A component that takes no input and always outputs an active signal.
 */
public class SignalSourceComponent extends Component {

    private final OutputPort northOutput;

    @ComponentConstructor
    public SignalSourceComponent() {
        this.northOutput = new OutputPort();
        this.tagToOutput = Map.ofEntries(
                Map.entry("output", this.northOutput)
        );

        setOutputState(this.northOutput, true);
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "SignalSource";
    }

    @Override
    public String getDisplayName() {
        return "Signal Source";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal to all connected outputs";
    }

    @Override
    protected void updateOutputs() {}

    public void toggle() {
        this.toggle(!this.northOutput.isHigh());
    }

    public void toggle(Boolean setHigh) {
        this.northOutput.setState(setHigh ? LogicState.HIGH : LogicState.LOW);
    }


}
