package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;

import java.util.HashSet;
import java.util.Set;

class CircuitController extends AnchorPane {

    @FXML private Pane backgroundPane;
    private CanvasGridPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;
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
    }

    void addComponent(Component component, int x, int y) {
        this.circuit.addComponent(component);
        this.ccSet.add(new ComponentController(component, x, y));
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
