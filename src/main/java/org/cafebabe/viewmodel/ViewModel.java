package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.Set;
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
    private final Set<ISelectable> selectableSet = new HashSet<>();
    private final ConnectionManager connectionManager;
    Event<ISelectable> onSelectableSelected = new Event<>();

    public ViewModel(Workspace workspace) {
        this.workspace = workspace;
        connectionManager = new ConnectionManager(this.workspace, this);
    }

    /* Public */
    public void tryConnectWire(InputPort inPort) {
        connectionManager.tryConnectWire(inPort);
    }

    public void tryConnectWire(OutputPort outPort) {
        connectionManager.tryConnectWire(outPort);
    }

    public boolean canConnectTo(Port port) {
        return connectionManager.canConnectTo(port);
    }

    public EmptyEvent onConnectionStateChanged() {
        return connectionManager.onConnectionStateChanged();
    }

    public boolean wireHasConnections() {
        return connectionManager.wireHasConnections();
    }

    public void broadcastConnectionState() {
        connectionManager.broadcastConnectionState();
    }

    public void abortSelections() {
        connectionManager.abortSelections();
    }

    public void addWire(Wire wire) {
        workspace.getCircuit().addWire(wire);
        onWireAdded.notifyListeners(wire);
    }

    public void addComponent(Component component) {
        workspace.getCircuit().addComponent(component);
        onComponentAdded.notifyListeners(component);
    }

    public void selectComponent(ISelectable selectable) {
        selectableSet.add(selectable);
        selectable.select();
    }

    public void deselectComponent(ISelectable selectable) {
        selectableSet.remove(selectable);
        selectable.deselect();
    }

    public void deleteSelectedComponents() {
        for (ISelectable selectable : selectableSet) {
            selectable.destroy();
        }
        selectableSet.clear();
    }

    public void deleteSelectedControllers() {
        controllerSelector.deleteSelectedControllers();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        controllerSelector.handleControllerClick(component, event);
    }

    public void clearSelection() {
        controllerSelector.clearSelection();
    }
}
