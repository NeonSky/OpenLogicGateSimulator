package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.InputPort;

public class InPortController extends PortController {
    private InputPort port;

    public InPortController(String name, double x, double y, InputPort port, IWireConnector wireConnector) {
        super(name, x, y, wireConnector);
        connectionNodeCircle.getStyleClass().add("inPort");
        this.port = port;
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState(IConnectionState connectionState) {
        if(this.port.isConnected()) {
            connectionNodeCircle.getStyleClass().add("connected");
            if(this.port.isActive()) {
                connectionNodeCircle.getStyleClass().add("active");
            } else {
                connectionNodeCircle.getStyleClass().remove("active");
            }
        } else {
            if (wireConnector.canConnectTo(this.port) && wireConnector.wireHasConnections()) {
                connectionNodeCircle.getStyleClass().add("candidate");
            } else {
                connectionNodeCircle.getStyleClass().remove("candidate");
            }
        }
    }

    private void connectIfPossible() {
        wireConnector.tryConnectWire(this, this.port);
    }
}
