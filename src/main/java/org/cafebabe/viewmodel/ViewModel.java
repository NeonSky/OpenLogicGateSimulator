package org.cafebabe.viewmodel;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

@SuppressWarnings("PMD.TooManyMethods")
public class ViewModel {
    public final Event<Component> onComponentAdded = new Event<>();
    public final Event<Wire> onWireAdded = new Event<>();
    public final Event<Node> onDragSelectionDetected = new Event<>();
    public final Event<Node> onDragSelectionReleased = new Event<>();
    private final Workspace workspace;
    private final ControllerSelector controllerSelector = new ControllerSelector();
    private final SelectionBox selectionBox = new SelectionBox();
    private final ConnectionManager connectionManager;
    private final Camera camera = new Camera();
    private Position mouseDragPreviousPos;

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
        this.controllerSelector.clearSelection();
        this.connectionManager.abortWireConnection();
    }

    public void addWire(Wire wire) {
        this.workspace.getCircuit().addWire(wire);
        this.onWireAdded.notifyListeners(wire);
    }

    public void addComponent(Component component) {
        this.workspace.getCircuit().addComponent(component);
        this.onComponentAdded.notifyListeners(component);
    }

    public void selectComponents(List<ISelectable> selectables) {
        this.controllerSelector.select(selectables);
    }

    public void deleteSelectedControllers() {
        for (ISelectable component : this.controllerSelector.getSelectedComponents()) {
            this.workspace.getCircuit().removeItem(component.getModelObject());
            component.getModelObject().destroy();
        }
        this.connectionManager.broadcastConnectionState();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        this.controllerSelector.handleControllerClick(component, event);
    }

    public void handleMouseDragged(MouseEvent event) {
        if (this.mouseDragPreviousPos == null) {
            this.mouseDragPreviousPos = new Position((int)event.getX(), (int)event.getY());
        }
        /*TODO tools, with an enum instead of these placeholder strings*/
        String selectedTool = "pan";
        switch (selectedTool) {
            case "pan":
                this.camera.pan(
                        event.getX() - this.mouseDragPreviousPos.getX(),
                        event.getY() - this.mouseDragPreviousPos.getY()
                );
                break;
            case "selectionbox":
                this.selectionBox.handleMouseDragged(event);
                if (event.isDragDetect()) {
                    Node selectionBox = this.selectionBox.getSelectionBox();
                    this.onDragSelectionDetected.notifyListeners(selectionBox);
                }
                break;
            default:
                break;
        }

        this.mouseDragPreviousPos = new Position((int)event.getX(), (int)event.getY());
    }

    public void handleMouseDragReleased(MouseEvent event) {
        this.mouseDragPreviousPos = null;
        if (!this.selectionBox.hasBox()) {
            return;
        }
        Node selectionRect = this.selectionBox.getSelectionBox();
        this.onDragSelectionReleased.notifyListeners(selectionRect);
        this.selectionBox.handleMouseDragReleased();
    }

    public void addTransformable(ITransformable transformable) {
        this.camera.addTransformable(transformable);
    }
}
