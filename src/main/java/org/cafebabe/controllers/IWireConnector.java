package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;

import java.util.function.Consumer;

public interface IWireConnector {
    void tryConnectWire(InPortController inPortController, InputPort inPort);
    void tryConnectWire(OutPortController outPortController, OutputPort outPort);
    void addConnectionStateListener(Consumer<IConnectionState> stateListener);
    void removeConnectionStateListener(Consumer<IConnectionState> stateListener);
    boolean wireHasConnections();
    boolean canConnectTo(Port port);
}
