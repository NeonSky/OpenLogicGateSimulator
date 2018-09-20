package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.IPort;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;

import java.util.function.Consumer;

public interface IWireConnector {
    void tryConnectWire(InPortController inPortController, InputPort inPort);
    void tryConnectWire(OutPortController outPortController, OutputPort outPort);
    void addConnectionStateListener(Consumer<IConnectionState> stateListener);
    void removeConnectionStateListener(Consumer<IConnectionState> stateListener);
    boolean wireHasConnections();
    boolean canConnectTo(IPort port);
}
