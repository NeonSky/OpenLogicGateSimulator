package org.cafebabe.model.editor.workspace.circuit.component.source;

import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A component that takes no input and always outputs an active signal.
 */
public class SignalSourceComponent extends Component {

    @ComponentConstructor
    public SignalSourceComponent() {
        OutputPort northOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", northOutput));
        setOutputState(northOutput, true);
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

}
