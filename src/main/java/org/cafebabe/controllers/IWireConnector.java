package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;

public interface IWireConnector {
    void tryConnectWire(InPortController inPortController, InputPort inPort);
    void tryConnectWire(OutPortController outPortController, OutputPort outPort);
    boolean canConnectTo(InputPort port);
    boolean canConnectTo(OutputPort port);
    void addConnectionStateListener(IConnectionStateListener stateListener);
    void removeConnectionStateListener(IConnectionStateListener stateListener);
    boolean wireHasConnections();
}

interface IConnectionStateListener {
    void handle(IConnectionState state);
}
