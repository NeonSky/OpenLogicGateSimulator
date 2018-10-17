package org.cafebabe.removemeplz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.cafebabe.model.editor.workspace.camera.Camera;
import org.cafebabe.model.editor.workspace.camera.IHaveTransform;
import org.cafebabe.model.editor.workspace.selection.ConnectionManager;
import org.cafebabe.model.editor.workspace.selection.ControllerSelector;
import org.cafebabe.model.editor.workspace.selection.ISelectable;
import org.cafebabe.model.editor.workspace.selection.SelectionBox;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;

/**
 * Allows for communication between view-specific controllers.
 * Some calls are first delegated to different handlers (selection, connection etc.)
 * Used for things that should not concern the model,
 * but still involves communication between parts of the view.
 */
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
    private static final double ZOOM_FACTOR = 1.001;
    private Position mouseDragPreviousPos;
    private Position mousePos = new Position(0,0);
    private final Map<Tool, EventHandler<MouseEvent>> toolToMethod = new HashMap<>();

    public ViewModel(Workspace workspace) {
        this.workspace = workspace;
        this.connectionManager = new ConnectionManager(this);

        this.toolToMethod.put(Tool.PAN, this::panCamera);
        this.toolToMethod.put(Tool.SELECT, this::dragSelectionBox);
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

    public void removeWire(Wire wire) {
        this.workspace.getCircuit().removeItem(wire);
    }

    public void removeComponent(Component component) {
        this.workspace.getCircuit().removeItem(component);
    }

    public void selectComponents(List<ISelectable> selectables) {
        this.controllerSelector.select(selectables);
    }

    public void deleteSelectedControllers() {
        this.controllerSelector.getSelectedComponents().forEach(ISelectable::destroy);
        this.connectionManager.broadcastConnectionState();
        abortSelections();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        this.controllerSelector.handleControllerClick(component, event);
    }

    public void handleScrollEvent(ScrollEvent event) {
        zoomCamera(event);
    }

    public void handleMouseMoved(MouseEvent event) {
        this.mousePos = new Position((int)event.getX(), (int)event.getY());
    }

    public void handleMouseDragged(MouseEvent event) {
        if (this.mouseDragPreviousPos == null) {
            this.mouseDragPreviousPos = new Position((int)event.getX(), (int)event.getY());
        }

        Tool selectedTool = event.isControlDown() ? Tool.PAN : Tool.SELECT;
        this.toolToMethod.get(selectedTool).handle(event);

        this.mouseDragPreviousPos = new Position((int)event.getX(), (int)event.getY());
        event.consume();
    }

    public Camera getCamera() {
        return this.camera;
    }

    private void dragSelectionBox(MouseEvent event) {
        this.selectionBox.handleMouseDragged(event);
        if (event.isDragDetect()) {
            Node selectionBox = this.selectionBox.getSelectionBox();
            this.onDragSelectionDetected.notifyListeners(selectionBox);
        }
    }

    private void panCamera(MouseEvent event) {
        this.camera.pan(
                event.getX() - this.mouseDragPreviousPos.getX(),
                event.getY() - this.mouseDragPreviousPos.getY()
        );
    }

    private void zoomCamera(ScrollEvent event) {
        this.camera.zoom(
                Math.pow(ZOOM_FACTOR, event.getDeltaY()),
                this.mousePos.getX(),
                this.mousePos.getY()
        );
    }

    public void handleMouseDragReleased(MouseEvent event) {
        this.mouseDragPreviousPos = null;
        if (!this.selectionBox.hasBox()) {
            return;
        }
        Node selectionRect = this.selectionBox.getSelectionBox();
        this.onDragSelectionReleased.notifyListeners(selectionRect);
        this.selectionBox.handleMouseDragReleased();
        event.consume();
    }

    public void addTransform(IHaveTransform transform) {
        this.camera.addTransform(transform);
    }
}
