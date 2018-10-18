package org.cafebabe.model.editor.workspace.circuit.component.source;

import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * A component that takes no input and outputs either a high or low signal, which can be toggled.
 */
public class SignalSourceComponent extends Component {

    private final OutputPort signalOutput;

    @ComponentConstructor
    public SignalSourceComponent() {
        super("SIGNAL_Source", "Signal Source", "Emits a high or low signal, can be toggled.");

        this.signalOutput = new OutputPort();
        this.tagToOutput = Map.ofEntries(
                Map.entry("output", this.signalOutput)
        );

        this.signalOutput.setLogicState(LogicState.HIGH);
    }

    @Override
    protected void updateOutputs() {}

    public boolean isActive() {
        return this.signalOutput.isHigh();
    }

    public void toggle() {
        this.toggle(!this.signalOutput.isHigh());
    }

    public void toggle(boolean setHigh) {
        this.signalOutput.setLogicState(setHigh ? LogicState.HIGH : LogicState.LOW);
        this.getOnUpdate().notifyListeners();
    }
}
