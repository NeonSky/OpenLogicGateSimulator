package org.cafebabe.controllers.editor.workspace;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.editor.workspace.component.ComponentController;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.viewmodel.ViewModel;

class CircuitController extends AnchorPane {

    private final ComponentDragDropHandler componentDragDropHandler;
    private final ViewModel viewModel;
    @FXML private Pane backgroundPane;
    @FXML private Pane componentPane;
    private CanvasGridPane gridPane;

    CircuitController(ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.onComponentAdded.addListener(this::addComponent);
        viewModel.onWireAdded.addListener(this::addWire);
        viewModel.onDragSelectionDetected.addListener(this::addSelectionBox);
        viewModel.onDragSelectionReleased.addListener(this::removeSelectionBox);
        setupFxml();
        this.componentDragDropHandler = new ComponentDragDropHandler(this.viewModel);
    }

    /* Package-Private */
    /* Private */
    private void setupFxml() {
        FxmlUtil.attachFxml(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.gridPane = new CanvasGridPane();
        FxmlUtil.scaleWithAnchorPaneParent(this.gridPane);
        this.getChildren().add(this.gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(this.componentPane);
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();

        FxmlUtil.onSceneClick(this, this::handleMouseClick);
        FxmlUtil.onSceneKeyPress(this, this::handleKeyPress);
        FxmlUtil.onMouseDragged(this.componentPane, viewModel::handleMouseDragged);
        FxmlUtil.onMouseDragReleased(this.componentPane, viewModel::handleMouseDragReleased);
    }

    private void addWire(Wire wire) {
        WireController wireController = new WireController(wire);
        this.componentPane.getChildren().add(wireController.getWireLine());
        wireController.getWireLine().toBack();
        wireController.addClickListener(event ->
                this.viewModel.handleControllerClick(wireController, event)
        );
        this.gridPane.toBack();
    }

    private void addComponent(Component component) {
        ComponentController newCompController = new ComponentController(component, this.viewModel);
        this.componentPane.getChildren().add(newCompController);
        newCompController.setOnDragDetected(event ->
                this.componentDragDropHandler.onComponentDragDetected(newCompController, event)
        );
        newCompController.addClickListener(event ->
                this.viewModel.handleControllerClick(newCompController, event)
        );
    }

    private void addSelectionBox(Node rectangle) {
        this.componentPane.getChildren().add(rectangle);
    }

    private void removeSelectionBox(Node rectangle) {
        this.componentPane.getChildren().remove(rectangle);
    }

    private void handleMouseClick(MouseEvent event) {
        if (event.getTarget() == this.componentPane) {
            this.viewModel.abortSelections();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.viewModel.abortSelections();
        } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            this.viewModel.deleteSelectedControllers();
        }
    }

    private void onComponentDragDetected(ComponentController componentController,
                                         MouseEvent event) {

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */

        this.componentDragDropHandler.onComponentDragDetected(componentController, event);
    }

    @FXML
    private void onComponentPaneDragEntered(DragEvent event) {
        this.componentDragDropHandler.onComponentPaneDragEntered(event);
    }

    @FXML
    private void onComponentPaneDragExited(DragEvent event) {
        this.componentDragDropHandler.onComponentPaneDragExited(event);
    }

    @FXML
    private void onComponentPaneDragDropped(DragEvent event) {
        this.componentDragDropHandler.onComponentPaneDragDropped(event);
    }

    @FXML
    private void onComponentPaneDragOver(DragEvent event) {
        this.componentDragDropHandler.onComponentPaneDragOver(event);
    }


}
