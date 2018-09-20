package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.InputPort;

import java.util.ArrayList;
import java.util.List;

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
        List<String> styleClasses = new ArrayList<>();
        styleClasses.add("inPort");
        if(this.port.isConnected()){
            styleClasses.add("connected");
            if(this.port.isActive()) {
                styleClasses.add("active");
            }
        } else if (wireConnector.canConnectTo(this.port) && wireConnector.wireHasConnections()) {
            styleClasses.add("candidate");
        }
        connectionNodeCircle.getStyleClass().setAll(styleClasses);
    }

    private void connectIfPossible() {
        wireConnector.tryConnectWire(this, this.port);
    }
}
