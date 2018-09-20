package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.OutputPort;

import java.util.ArrayList;
import java.util.List;

public class OutPortController extends PortController {
    private OutputPort port;

    public OutPortController(String name, double x, double y, OutputPort port, IWireConnector wireConnector) {
        super(name, x, y, wireConnector);
        this.port = port;
        this.connectionNodeCircle.getStyleClass().add("outPort");
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState(IConnectionState connectionState) {
        computeAndSetStyleClasses(this.port, "outPort");
    }

    private void connectIfPossible() {
        this.wireConnector.tryConnectWire(this, this.port);
    }
}
