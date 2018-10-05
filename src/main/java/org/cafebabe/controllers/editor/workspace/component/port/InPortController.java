package org.cafebabe.controllers.editor.workspace.component.port;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.viewmodel.ViewModel;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortController extends PortController {
    private final InputPort port;

    public InPortController(double x, double y, InputPort port, ViewModel viewModel) {
        super(x, y, viewModel);
        this.connectionNodeCircle.getStyleClass().add("inPort");
        this.port = port;
    }

    /* Private */
    private void connectIfPossible() {
        viewModel.tryConnectWire(this.port);
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState() {
        computeAndSetStyleClasses(this.port, "inPort");
    }
}
