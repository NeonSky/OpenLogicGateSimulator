package org.cafebabe.controller.editor.workspace;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.editor.workspace.circuit.CircuitController;
import org.cafebabe.controller.editor.workspace.circuit.selection.ControllerSelector;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.controller.editor.workspace.circuit.selection.SelectionBox;
import org.cafebabe.model.editor.workspace.ConnectionManager;
import org.cafebabe.model.editor.workspace.camera.Camera;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.view.editor.workspace.WorkspaceView;
import org.cafebabe.view.editor.workspace.circuit.CircuitView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Handles user interactions with the workspace view.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class WorkspaceController extends Controller {

    private static final double MAX_ZOOM_SPEED = 1.001;

    private final WorkspaceView view;
    private final Camera camera;
    private final ControllerSelector controllerSelector = new ControllerSelector();
    private final SelectionBox selectionBox = new SelectionBox();

    private Position mousePos = new Position(0,0);
    private Position mouseDragPreviousPos;


    public WorkspaceController(WorkspaceView view) {
        super(view);
        this.view = view;
        this.camera = view.getWorkspace().getCamera();
        setSubviewAttachController(CircuitView.class, CircuitController.class);
        view.init();
        setupComponentPaneEvents();

        ConnectionManager connectionManager = this.view.getWorkspace().getConnectionManager();
        this.view.getOnComponentAdded().addListener((c) -> {
            this.camera.addTransform(c);
            c.getComponentSvg().addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                    this.controllerSelector.handleControllerClick(c, event)
            );
            c.getPortViews().forEach(p -> {
                p.getConnectionNodeCircle().onMouseClickedProperty().setValue((e) -> {
                    connectionManager.connectToPort(p.getPort());
                });
            });
        });

        this.view.getOnWireAdded().addListener((w) -> {
            this.camera.addTransform(w);
            w.getWireLine().addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                    this.controllerSelector.handleControllerClick(w, event)
            );
        });

        connectionManager.onNewEditingWire.addListener(this.view::unhighlightPorts);
        connectionManager.onLookingForPortType.addListener((type) -> {
            if (type == InputPort.class) {
                this.view.highlightInputPorts();
            } else {
                this.view.highlightOutputPorts();
            }
        });

    }

    /* Private */
    private void abortSelections() {
        this.controllerSelector.clearSelection();
        this.view.getWorkspace().getConnectionManager().abortWireConnection();
    }

    private void updateMousePosition(MouseEvent event) {
        this.mousePos = new Position((int)event.getX(), (int)event.getY());
    }

    private void updatePreviousDragPos(MouseEvent event) {
        this.mouseDragPreviousPos = new Position((int)event.getX(), (int)event.getY());
    }

    private void zoomCamera(ScrollEvent event) {
        this.camera.zoom(
                Math.pow(MAX_ZOOM_SPEED, event.getDeltaY()),
                this.mousePos.getX(),
                this.mousePos.getY()
        );
    }

    private void panCamera(MouseEvent event) {
        this.camera.pan(
                event.getX() - this.mouseDragPreviousPos.getX(),
                event.getY() - this.mouseDragPreviousPos.getY()
        );
    }

    private void startDragSelection(MouseEvent event) {
        this.selectionBox.handleMouseDragged(event);
        if (event.isDragDetect()) {
            Node selectionBox = this.selectionBox.getBox();
            this.view.getComponentPane().getChildren().add(selectionBox);
        }
    }

    private void finishDragSelection() {
        Bounds selectionBounds = this.selectionBox.getBox().getBoundsInParent();
        List<ISelectable> selectedComponents = this.view.getComponentsInBounds(selectionBounds);
        this.controllerSelector.select(selectedComponents);

        this.view.getComponentPane().getChildren().remove(this.selectionBox.getBox());
    }

    private void handleMouseDragged(MouseEvent event) {
        if (this.mouseDragPreviousPos == null) {
            updatePreviousDragPos(event);
        }

        if (event.isControlDown()) {
            panCamera(event);
        } else {
            startDragSelection(event);
        }

        updatePreviousDragPos(event);
        event.consume();
    }

    private void handleMouseDragReleased(MouseEvent event) {
        this.mouseDragPreviousPos = null;
        if (this.selectionBox.hasBox()) {
            finishDragSelection();
            this.selectionBox.handleMouseDragReleased();
            event.consume();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            abortSelections();
        } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            deleteSelectedControllers();
        }
    }

    private void deleteSelectedControllers() {
        this.controllerSelector.getSelectedComponents().forEach(ISelectable::destroy);
        abortSelections();
    }

    private void setupComponentPaneEvents() {
        Node componentPane = this.view.getComponentPane();

        FxmlUtil.onInputEventWithMeAsTarget(componentPane,
                MouseEvent.MOUSE_PRESSED, (e) -> this.abortSelections());
        FxmlUtil.onMySceneLoaded(this.view, () ->
                FxmlUtil.onSceneInputEvent(this.view.getScene(),
                        KeyEvent.KEY_PRESSED, this::handleKeyPress)
        );

        FxmlUtil.onInputEventWithMeAsTarget(componentPane,
                MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        FxmlUtil.onInputEventWithMeAsTarget(componentPane,
                MouseEvent.MOUSE_RELEASED, this::handleMouseDragReleased);
        FxmlUtil.onInputEventWithMeAsTarget(componentPane,
                MouseEvent.MOUSE_MOVED, this::updateMousePosition);
        this.view.getComponentPane().setOnScroll(this::zoomCamera);
    }
}
