package org.cafebabe.controllers.editor.workspace.component.port;

import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.viewmodel.ViewModel;

/**
 * The "white circle" representing an outPort of a component.
 */
public class OutPortController extends PortController {
    private final OutputPort port;

    public OutPortController(double x, double y, OutputPort port, ViewModel connectionManager) {
        super(x, y, connectionManager);
        this.port = port;
        this.connectionNodeCircle.getStyleClass().add("outPort");
    }

    /* Private */
    private void connectIfPossible() {
        this.viewModel.tryConnectWire(this.port);
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState() {
        computeAndSetStyleClasses(this.port, "outPort");
    }
}
