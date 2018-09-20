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
        connectionNodeCircle.getStyleClass().add("outPort");
    }

    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState(IConnectionState connectionState) {
        List<String> styleClasses = new ArrayList<>();
        styleClasses.add("outPort");
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
        this.wireConnector.tryConnectWire(this, this.port);
    }
}
