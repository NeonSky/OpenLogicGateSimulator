package org.cafebabe.controller.editor.workspace.circuit.component.port;

import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortController extends PortController {
    private final InputPort port;

    public InPortController(InPortView view) {
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
        super.updateStyleClasses("inPort");
    }

    private void connectIfPossible() {
        viewModel.tryConnectWire(this.port);
    }

}
