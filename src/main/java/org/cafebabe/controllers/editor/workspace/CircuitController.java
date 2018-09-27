package org.cafebabe.controllers.editor.workspace;

import javafx.fxml.FXML;
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

public class CircuitController extends AnchorPane {

    private final ComponentDragDropHandler componentDragDropHandler;
    private final ViewModel viewModel;
    @FXML
    private Pane backgroundPane;
    @FXML
    private Pane componentPane;
    private CanvasGridPane gridPane;

    public CircuitController(ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.onComponentAdded.addListener(this::addComponent);
        viewModel.onWireAdded.addListener(this::addWire);
        setupFXML();
        componentDragDropHandler = new ComponentDragDropHandler(this.viewModel);
    }

    /* Package-Private */
    /* Private */
    private void setupFXML() {
        FxmlUtil.attachFXML(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        gridPane = new CanvasGridPane();
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
        this.getChildren().add(gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(componentPane);
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();

        FxmlUtil.onSceneClick(this, this::handleMouseClick);
        FxmlUtil.onSceneKeyPress(this, this::handleKeyPress);
    }

    private void addWire(Wire wire) {
        WireController wireController = new WireController(wire);
        this.getChildren().add(wireController.getWireLine());
        wireController.getWireLine().toBack();
        wireController.addClickListener(event -> viewModel.handleControllerClick(wireController, event));
        gridPane.toBack();
    }

    private void addComponent(Component component) {
        ComponentController newCompController = new ComponentController(component, viewModel);
        this.getChildren().add(newCompController);
        newCompController.setOnDragDetected((event) -> componentDragDropHandler.onComponentDragDetected(newCompController, event));
        newCompController.addClickListener(event -> viewModel.handleControllerClick(newCompController, event));

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

    private void onComponentDragDetected(ComponentController componentController, MouseEvent event) {

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */

        componentDragDropHandler.onComponentDragDetected(componentController, event);
    }

    @FXML
    private void onComponentPaneDragEntered(DragEvent event) {
        componentDragDropHandler.onComponentPaneDragEntered(event);
    }

    @FXML
    private void onComponentPaneDragExited(DragEvent event) {
        componentDragDropHandler.onComponentPaneDragExited(event);
    }

    @FXML
    private void onComponentPaneDragDropped(DragEvent event) {
        componentDragDropHandler.onComponentPaneDragDropped(event);
    }

    @FXML
    private void onComponentPaneDragOver(DragEvent event) {
        componentDragDropHandler.onComponentPaneDragOver(event);
    }


}
