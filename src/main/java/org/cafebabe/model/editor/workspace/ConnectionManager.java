package org.cafebabe.model.editor.workspace;

import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;


/**
 * A class for managing the process of creating wires.
 */
public class ConnectionManager {

    public final Event<Class<? extends Port>> onLookingForPortType = new Event<>();
    public final EmptyEvent onNewEditingWire = new EmptyEvent();
    final Event<Wire> onAddWire = new Event<>();
    private Wire editingWire;


    public ConnectionManager() {
        this.editingWire = new Wire();
    }

    /* Public */
    public void connectToPort(Port port) {
        if (port instanceof InputPort) {
            connectToPort((InputPort) port);
        } else if (port instanceof OutputPort) {
            connectToPort((OutputPort) port);
        } else {
            throw new InvalidPortTypeException();
        }
    }

    public void connectToPort(InputPort inPort) {
        if (canConnectTo(inPort)) {
            this.editingWire.connectInputPort(inPort);

            if (isWireDone()) {
                finishEditingWire();
            } else {
                this.onLookingForPortType.notifyListeners(OutputPort.class);
            }
        }
    }

    public void connectToPort(OutputPort outPort) {
        this.editingWire.connectOutputPort(outPort);

        if (isWireDone()) {
            finishEditingWire();
        } else {
            this.onLookingForPortType.notifyListeners(InputPort.class);
        }
    }

    public void abortWireConnection() {
        this.editingWire.destroy();
        newEditingWire();
    }

    /* Private */
    private void newEditingWire() {
        this.editingWire = new Wire();
        this.onNewEditingWire.notifyListeners();
    }

    private void finishEditingWire() {
        this.onAddWire.notifyListeners(this.editingWire);
        newEditingWire();
    }

    private boolean canConnectTo(InputPort port) {
        return !this.editingWire.isAnyInputConnected() && !port.isConnected();
    }

    private boolean isWireDone() {
        return this.editingWire.isAnyInputConnected() && this.editingWire.isAnyOutputConnected();
    }

}
