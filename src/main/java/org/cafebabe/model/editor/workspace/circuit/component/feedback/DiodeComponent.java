package org.cafebabe.model.editor.workspace.circuit.component.feedback;

import java.util.Map;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * A component that lights up on a high signal.
 */
public class DiodeComponent extends Component {

    private final InputPort input;

    @ComponentConstructor
    public DiodeComponent() {
        super("DIODE_Component", "Diode", "Is lit up bright yellow on high signal.");
        this.input = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input", this.input)
        );

        this.input.getOnStateChanged().addListener(p -> getOnUpdate().notifyListeners());
    }

    public boolean isLit() {
        return this.input.isHigh();
    }
}
