package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Position;

import java.util.HashSet;
import java.util.Set;

class CircuitController extends AnchorPane {

    @FXML private Pane backgroundPane;
    private CanvasGridPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;
    private Position dragStartedPosition;
    private Set<ComponentController> ccSet = new HashSet<>();

    CircuitController(Circuit circuit) {
        this.circuit = circuit;

        FxmlUtil.attachFXML(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        gridPane = new CanvasGridPane();
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
        this.getChildren().add(gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(componentPane);
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();

        componentPane.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof ComponentController) {
                /* Accept the event if the dragged item is a component controller instance */
                event.acceptTransferModes(TransferMode.ANY);

                ComponentController cc = (ComponentController)event.getGestureSource();
                cc.getPosition().translate((int)event.getX() - cc.getPosition().getX() - dragStartedPosition.getX(),
                                           (int)event.getY() - cc.getPosition().getY() - dragStartedPosition.getY());
                update();
            }

            event.consume();
        });
    }

    void addComponent(Component component, int x, int y) {
        this.circuit.addComponent(component);

        ComponentController cc = new ComponentController(component, x, y);


        cc.setOnDragDetected(event -> {
            Dragboard db = cc.startDragAndDrop(TransferMode.ANY);

            /* Need to add something (anything) to Dragboard, otherwise
             * the drag does not register on the target */
            ClipboardContent c1 = new ClipboardContent();
            c1.put(DataFormat.PLAIN_TEXT, "foo");
            db.setContent(c1);

            dragStartedPosition = new Position((int)event.getX(), (int)event.getY());

            event.consume();
        });

        this.ccSet.add(cc);
        update();
    }

    void removeComponent(ComponentController component) {
        this.circuit.removeComponent(component.getComponent());
        this.ccSet.remove(component);
        update();
    }

    private void update() {
        this.componentPane.getChildren().clear();

        for (ComponentController cc : this.ccSet) {
            this.componentPane.getChildren().add(cc);
            cc.setLayoutX(cc.getPosition().getX());
            cc.setLayoutY(cc.getPosition().getY());
        }
    }

}
