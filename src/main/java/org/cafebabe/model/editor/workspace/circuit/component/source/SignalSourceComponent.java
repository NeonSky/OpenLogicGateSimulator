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
        super("SignalSource", "Signal Source", "Emits an active signal to all connected outputs");

        OutputPort northOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", northOutput));
        setOutputState(northOutput, true);
    }

    @Override
    protected void updateOutputs() {}

}
