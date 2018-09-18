package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Workspace;

class WorkspaceController extends AnchorPane {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    private final Workspace workspace;
    private final CircuitController circuitController;

    WorkspaceController(Workspace workspace) {
        FxmlUtil.attachFXML(this, "/view/WorkspaceView.fxml");

        this.workspace = workspace;
        this.circuitController = new CircuitController(this.workspace.getCircuit());
        this.circuitAnchorPane.getChildren().add(this.circuitController);

        FxmlUtil.scaleWithAnchorPaneParent(workspaceRoot);
    }

    void addComponent(Component component, int x, int y) {
        this.circuitController.addComponent(component, x, y);
    }

    void removeComponent(ComponentController component) {
        this.circuitController.removeComponent(component);
    }

}
