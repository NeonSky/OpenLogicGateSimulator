package org.cafebabe.controller.editor.workspace.circuit.component.port;

import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;

/**
 * The "white circle" representing an output port of a component.
 */
public class OutPortController extends PortController {

    public OutPortController(OutPortView view) {
        super(view);
        OutputPort port = view.getPort();
        view.setPersistentStyles(List.of("outPort"));
        port.getOnStateChanged().addListener((p) -> updateStyleClasses());
        updateStyleClasses();
    }

    /* Protected */
    @Override
    protected void handleUpdatedConnectionState() {
        updateStyleClasses();
    }

}
