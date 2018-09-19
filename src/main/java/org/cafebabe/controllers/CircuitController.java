package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.util.ComponentUtil;
import org.cafebabe.model.workspace.Position;

import java.util.HashSet;
import java.util.Set;

class CircuitController extends AnchorPane {

    @FXML private Pane backgroundPane;
    private CanvasGridPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;
    private Position dragStartedPosition;
    private ComponentController dragNewComponentController;
    private Set<ComponentController> ccSet = new HashSet<>();
    private Set<WireController> wireSet = new HashSet<>();

    CircuitController(Circuit circuit) {
        this.circuit = circuit;

        setupFXML();
    }

    private void setupFXML() {
        FxmlUtil.attachFXML(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        gridPane = new CanvasGridPane();
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
        this.getChildren().add(gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(componentPane);
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();
    }

    @FXML
    private void onComponentPaneDragEntered(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            ComponentListCellController componentListCellController = (ComponentListCellController)event.getGestureSource();

            Component newComponent = ComponentUtil.componentFactory(componentListCellController.getComponentName());

            dragNewComponentController = addComponent(newComponent, (int)event.getX(), (int)event.getY());

            event.consume();
        }
    }

    @FXML
    private void onComponentPaneDragExited(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController && dragNewComponentController != null) {
            removeComponent(dragNewComponentController);
            event.consume();
        }
    }

    @FXML
    private void onComponentPaneDragDropped(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            event.setDropCompleted(true);
            event.consume();
            dragNewComponentController = null;
        }
    }

    @FXML
    private void onComponentPaneDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentController) {
            /* Handle the event if the dragged item is a component controller instance */
            handleComponentDragOver(event);
        } else if (event.getGestureSource() instanceof ComponentListCellController) {
            /* Accept the event if the dragged item is a component list cell controller instance */
            handleComponentListCellDragOver(event);
        }

        event.consume();
    }

    private void handleComponentDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        ComponentController componentController = (ComponentController)event.getGestureSource();
        componentController.getPosition().translate((int)event.getX() - componentController.getPosition().getX() - dragStartedPosition.getX(),
                                                    (int)event.getY() - componentController.getPosition().getY() - dragStartedPosition.getY());

        refreshComponentPane();
    }

    private void handleComponentListCellDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        ComponentListCellController componentListCellController = (ComponentListCellController)event.getGestureSource();

        double height = dragNewComponentController.getHeight();
        double width = dragNewComponentController.getWidth();

        if (!componentListCellController.getComponentName().equals(dragNewComponentController.getComponent().getDisplayName())) {
            throw new RuntimeException("Dragged component from component list is not equal to currently dragged component");
        }

        dragNewComponentController.getPosition().move((int)(event.getX() - (width / 2)), (int)(event.getY() - (height / 2)));
        refreshComponentPane();
    }

    ComponentController addComponent(Component component, int x, int y) {
        this.circuit.addComponent(component);

        ComponentController newCompController = new ComponentController(component, x, y);
        newCompController.setOnDragDetected((event) -> onComponentDragDetected(newCompController, event));
        this.ccSet.add(newCompController);

        refreshComponentPane();

        return newCompController;
    }

    private void onComponentDragDetected(ComponentController componentController, MouseEvent event) {
        Dragboard dragboard = componentController.startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent dummyContent = new ClipboardContent();
        dummyContent.put(DataFormat.PLAIN_TEXT, "foo");
        dragboard.setContent(dummyContent);

        dragStartedPosition = new Position((int)event.getX(), (int)event.getY());

        event.consume();
    }

    void removeComponent(ComponentController component) {
        this.circuit.removeComponent(component.getComponent());
        this.ccSet.remove(component);
        refreshComponentPane();
    }

    private void refreshComponentPane() {
        this.componentPane.getChildren().clear();

        for (ComponentController componentController : this.ccSet) {
            this.componentPane.getChildren().add(componentController);
            componentController.setLayoutX(componentController.getPosition().getX());
            componentController.setLayoutY(componentController.getPosition().getY());
        }

        for (WireController wireController : this.wireSet) {
            this.componentPane.getChildren().add(wireController.getWireLine());
            wireController.getWireLine().toBack();
        }
    }
}
