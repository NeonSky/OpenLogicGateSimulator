package org.cafebabe.model.editor.workspace.selection;

import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.removemeplz.ViewModel;


/**
 * A class for managing the process of creating wires and updating the model when clicking on ports.
 */
public class ConnectionManager {

    private final EmptyEvent onStateChanged = new EmptyEvent();
    private final ViewModel viewModel;
    private Wire wire;


    public ConnectionManager(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Public */
    public boolean canConnectTo(Port port) {
        if (port instanceof InputPort) {
            return !createWireIfNeeded().isAnyInputConnected() && !port.isConnected();
        } else if (port instanceof OutputPort) {
            return !createWireIfNeeded().isAnyOutputConnected() && !port.isConnected();
        } else {
            throw new InvalidPortTypeException();
        }
    }

    public void broadcastConnectionState() {
        this.onStateChanged.notifyListeners();
    }

    public void tryConnectWire(InputPort inPort) {
        if (canConnectTo(inPort)) {
            createWireIfNeeded();
            this.wire.connectInputPort(inPort);

            broadcastConnectionState();

            if (this.wire.isAnyInputConnected() && this.wire.isAnyOutputConnected()) {
                finishEditingWire();
            }
        }
    }

    public void tryConnectWire(OutputPort outPort) {
        if (canConnectTo(outPort)) {
            createWireIfNeeded();
            this.wire.connectOutputPort(outPort);

            broadcastConnectionState();

            if (this.wire.isAnyInputConnected() && this.wire.isAnyOutputConnected()) {
                finishEditingWire();
            }
        }
    }

    public EmptyEvent onConnectionStateChanged() {
        return this.onStateChanged;
    }

    public boolean wireHasConnections() {
        return this.createWireIfNeeded().isAnyOutputConnected()
                || this.createWireIfNeeded().isAnyInputConnected();
    }

    public void abortWireConnection() {
        if (this.wire != null) {
            this.wire.destroy();
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
