package org.cafebabe.model.editor.workspace.circuit.component.source;

import java.util.Map;
import java.util.Set;
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
        super("SIGNAL_Source", "Signal Source", "Emits an active signal to all connected outputs");

        this.northOutput = new OutputPort();
        this.tagToOutput = Map.ofEntries(
                Map.entry("output", this.northOutput)
        );

        setOutputState(this.northOutput, true);
    }

    @Override
    protected void updateOutputs() {}

    @Override
    public void trigger(Set<String> tags) {
        if (tags.contains("toggle-active")) {
            this.toggle();
        }
    }

    public void toggle() {
        this.toggle(!this.northOutput.isHigh());
    }

    public void toggle(Boolean setHigh) {
        this.northOutput.setState(setHigh ? LogicState.HIGH : LogicState.LOW);
        if (setHigh) {
            this.addStateData("active");
        } else {
            this.removeStateData("active");
        }
    }


}
