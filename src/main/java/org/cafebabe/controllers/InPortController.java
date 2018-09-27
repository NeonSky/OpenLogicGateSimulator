package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.viewmodel.ViewModel;

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
