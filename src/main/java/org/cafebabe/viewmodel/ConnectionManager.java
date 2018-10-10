package org.cafebabe.viewmodel;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.util.EmptyEvent;


/**
 * A class for managing the process of creating wires and updating the model when clicking on ports.
 */
class ConnectionManager {

    private final EmptyEvent onStateChanged = new EmptyEvent();
    private final ViewModel viewModel;
    private Wire wire;


    ConnectionManager(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Package-Private */
    boolean canConnectTo(Port port) {
        if (port instanceof InputPort) {
            return !createWireIfNeeded().isAnyInputConnected() && !port.isConnected();
        } else if (port instanceof OutputPort) {
            return !createWireIfNeeded().isAnyOutputConnected() && !port.isConnected();
        } else {
            throw new InvalidPortTypeException();
        }
    }

    void broadcastConnectionState() {
        this.onStateChanged.notifyListeners();
    }

    void tryConnectWire(InputPort inPort) {
        if (canConnectTo(inPort)) {
            createWireIfNeeded();
            this.wire.connectInputPort(inPort);

            broadcastConnectionState();

            if (this.wire.isAnyInputConnected() && this.wire.isAnyOutputConnected()) {
                finishEditingWire();
            }
        }
    }

    void tryConnectWire(OutputPort outPort) {
        if (canConnectTo(outPort)) {
            createWireIfNeeded();
            this.wire.connectOutputPort(outPort);

            broadcastConnectionState();

            if (this.wire.isAnyInputConnected() && this.wire.isAnyOutputConnected()) {
                finishEditingWire();
            }
        }
    }

    EmptyEvent onConnectionStateChanged() {
        return this.onStateChanged;
    }

    boolean wireHasConnections() {
        return this.createWireIfNeeded().isAnyOutputConnected()
                || this.createWireIfNeeded().isAnyInputConnected();
    }

    void abortWireConnection() {
        if (this.wire != null) {
            createWireIfNeeded().disconnectAll();
            createWireIfNeeded().destroy();
            stopEditingWire();
        }
        this.broadcastConnectionState();
    }

    /* Private */
    private Wire createWireIfNeeded() {
        if (this.wire == null) {
            this.wire = new Wire();
        }
        return this.wire;
    }

    private void finishEditingWire() {
        this.viewModel.addWire(this.wire);
        stopEditingWire();
    }

    private void stopEditingWire() {
        this.wire = null;
    }

}
