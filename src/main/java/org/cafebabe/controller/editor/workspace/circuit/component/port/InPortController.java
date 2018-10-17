package org.cafebabe.controller.editor.workspace.circuit.component.port;

import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortController extends PortController {

    public InPortController(InPortView view) {
        super(view);
        view.setPersistentStyles(List.of("inPort"));
        InputPort port = view.getPort();
        port.onStateChangedEvent().addListener((p) -> updateStyleClasses());
        updateStyleClasses();
    }

    /* Protected */
    @Override
    protected void handleUpdatedConnectionState() {
        updateStyleClasses();
    }

}
