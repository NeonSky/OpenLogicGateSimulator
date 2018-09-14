package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.util.CanvasPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;

import java.util.HashSet;
import java.util.Set;

public class CircuitController extends AnchorPane {

    @FXML private Pane backgroundPane;
    private CanvasPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;
    private Set<ComponentController> ccSet = new HashSet<>();

    public CircuitController(Circuit circuit) {
        this.circuit = circuit;
        //this.componentPane.getChildren().addAll(this.ccSet);

        FxmlUtil.attachFXML(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        gridPane = new CanvasPane();
        gridPane.setStyle("-fx-background-color: darkblue;" );
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
        this.getChildren().add(gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(componentPane);
        this.componentPane.setStyle("-fx-background-color: magenta");
        this.componentPane.toFront();
    }

    private void drawGrid() {}

    public void addComponent(Component component, int x, int y) {
        this.circuit.addComponent(component);
        this.ccSet.add(new ComponentController(component, x, y));
        update();
    }

    public void removeComponent(ComponentController component) {
        this.circuit.removeComponent(component.getComponent());
        this.ccSet.remove(component);
        update();
    }

    public void update() {
        this.componentPane.getChildren().clear();
        this.componentPane.getChildren().addAll(ccSet);
    }

}
