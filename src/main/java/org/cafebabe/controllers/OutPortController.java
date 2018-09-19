package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.OutputPort;

public class OutPortController extends PortController {
    private OutputPort port;

    public OutPortController(String name, double x, double y, OutputPort port, IWireConnector wireConnector) {
        super(name, x, y, wireConnector);
        this.port = port;
        connectionNodeCircle.getStyleClass().add("outPort");
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState(IConnectionState connectionState) {
        if(this.port.isConnected()) {
            connectionNodeCircle.getStyleClass().add("connected");
            if(port.isActive()) {
                connectionNodeCircle.getStyleClass().add("active");
            } else {
                connectionNodeCircle.getStyleClass().remove("active");
            }
            connectionNodeCircle.getStyleClass().remove("candidate");
        } else {
            if (wireConnector.canConnectTo(this.port) && wireConnector.wireHasConnections()) {
                connectionNodeCircle.getStyleClass().add("candidate");
            } else {
                connectionNodeCircle.getStyleClass().remove("candidate");
            }
        }
    }

    private void connectIfPossible() {
        this.wireConnector.tryConnectWire(this, this.port);
    }
}
