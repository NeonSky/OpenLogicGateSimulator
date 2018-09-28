package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.Node;
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
    public final Event<Node> onDragSelectionDetected = new Event<>();
    public final Event<Node> onDragSelectionReleased = new Event<>();
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

    public void selectComponent(ISelectable selectable) {
        controllerSelector.select(selectable);
    }

    public void selectComponents(List<ISelectable> selectables) {
        controllerSelector.select(selectables);
    }

    public void deselectComponent(ISelectable selectable) {
        controllerSelector.deselect(selectable);
    }

    public void deselectComponents(List<ISelectable> selectables) {
        controllerSelector.deselect(selectables);
    }

    public void deleteSelectedControllers() {
        this.controllerSelector.deleteSelectedControllers();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        this.controllerSelector.handleControllerClick(component, event);
    }

    public void clearSelection() {
        this.controllerSelector.clearSelection();
    }

    public void handleMouseDragged(MouseEvent event) {
        controllerSelector.handleMouseDragged(event);
        Node selectionBox = controllerSelector.getSelectionBox();
        if (event.isDragDetect()) {
            this.onDragSelectionDetected.notifyListeners(selectionBox);
        }
    }

    public void handleMouseDragReleased(MouseEvent event) {
        Node selectionRect = controllerSelector.getSelectionBox();
        if (selectionRect == null) {
            return;
        }
        this.onDragSelectionReleased.notifyListeners(selectionRect);
        controllerSelector.handleMouseDragReleased(event);
    }
}
