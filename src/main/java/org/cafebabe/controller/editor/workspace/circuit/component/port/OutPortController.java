package org.cafebabe.controller.editor.workspace.circuit.component.port;

import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;

/**
 * The "white circle" representing an output port of a component.
 */
public class OutPortController extends PortController {
    private final OutputPort port;

    public OutPortController(OutPortView view) {
        super(view);
        this.port = view.getPort();
        this.port.onStateChangedEvent().addListener((p) -> updateStyleClasses());
        updateStyleClasses();
    }

    /* Protected */
    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState() {
        updateStyleClasses();
    }

    /* Private */
    private void updateStyleClasses() {
        super.updateStyleClasses("outPort");
    }

    private void connectIfPossible() {
        this.viewModel.tryConnectWire(this.port);
    }

}
