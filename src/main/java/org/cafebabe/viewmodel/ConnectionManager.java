package org.cafebabe.viewmodel;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.util.EmptyEvent;


class ConnectionManager {

    private final EmptyEvent onStateChanged = new EmptyEvent();
    private final ViewModel viewModel;
    private Wire wire;


    ConnectionManager(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Public */
    /* Package-Private */
    boolean canConnectTo(Port port) {
        if (port instanceof InputPort) {
            return !createWireIfNeeded().isAnyInputConnected() && !port.isConnected();
        } else if (port instanceof OutputPort) {
            return !createWireIfNeeded().isAnyOutputConnected() && !port.isConnected();
        } else {
            throw new RuntimeException("Invalid Port Type!");
        }
    }

    void broadcastConnectionState() {
        this.onStateChanged.notifyListeners();
    }

    void tryConnectWire(InputPort inPort) {
        if (canConnectTo(inPort)) {
            Wire wire = createWireIfNeeded();
            wire.connectInputPort(inPort);

            if (wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                stopEditingWire();
            }

            broadcastConnectionState();
            this.onStateChanged.notifyListeners();
        }
    }

    void tryConnectWire(OutputPort outPort) {
        if (canConnectTo(outPort)) {
            Wire wire = this.createWireIfNeeded();
            wire.connectOutputPort(outPort);

            if (wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                stopEditingWire();
            }

            broadcastConnectionState();
            this.onStateChanged.notifyListeners();
        }
    }

    EmptyEvent onConnectionStateChanged() {
        return this.onStateChanged;
    }

    boolean wireHasConnections() {
        return this.createWireIfNeeded().isAnyOutputConnected()
                || this.createWireIfNeeded().isAnyInputConnected();
    }

    void abortSelections() {
        abortWireConnection();
        this.viewModel.clearSelection();
    }

    /* Private */
    private Wire createWireIfNeeded() {
        if (this.wire == null) {
            this.wire = new Wire();
            this.viewModel.addWire(this.wire);
        }
        return this.wire;
    }

    private void stopEditingWire() {
        this.wire = null;
    }

    private void abortWireConnection() {
        if (this.wire != null) {
            this.createWireIfNeeded().disconnectAll();
            this.createWireIfNeeded().destroy();
        }
        this.stopEditingWire();
        this.broadcastConnectionState();
    }

}
