package org.cafebabe.viewmodel;

import javafx.scene.input.MouseEvent;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class ViewModel {
    public final Event<Component> onComponentAdded = new Event<>();
    public final Event<Wire> onWireAdded = new Event<>();
    private final Workspace workspace;
    private final ControllerSelector controllerSelector = new ControllerSelector();
    private final ConnectionManager connectionManager;

    public ViewModel(Workspace workspace) {
        this.workspace = workspace;
        this.connectionManager = new ConnectionManager(this);
    }

    /* Public */
    public void tryConnectWire(InputPort inPort) {
        this.connectionManager.tryConnectWire(inPort);
    }

    public void tryConnectWire(OutputPort outPort) {
        this.connectionManager.tryConnectWire(outPort);
    }

    public boolean canConnectTo(Port port) {
        return this.connectionManager.canConnectTo(port);
    }

    public EmptyEvent onConnectionStateChanged() {
        return this.connectionManager.onConnectionStateChanged();
    }

    public boolean wireHasConnections() {
        return this.connectionManager.wireHasConnections();
    }

    public void abortSelections() {
        this.connectionManager.abortSelections();
    }

    public void addWire(Wire wire) {
        this.workspace.getCircuit().addWire(wire);
        this.onWireAdded.notifyListeners(wire);
    }

    public void addComponent(Component component) {
        this.workspace.getCircuit().addComponent(component);
        this.onComponentAdded.notifyListeners(component);
    }

    public void deleteSelectedControllers() {
        this.controllerSelector.deleteSelectedControllers(this.workspace.getCircuit());
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        this.controllerSelector.handleControllerClick(component, event);
    }

    public void clearSelection() {
        this.controllerSelector.clearSelection();
    }
}
