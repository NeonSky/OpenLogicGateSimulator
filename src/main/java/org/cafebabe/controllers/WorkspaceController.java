package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Workspace;

public class WorkspaceController extends AnchorPane {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    private final Workspace ws;
    private final CircuitController circuitController;

    public WorkspaceController(Workspace ws) {
        FxmlUtil.attachFXML(this, "/view/WorkspaceView.fxml");

        this.ws = ws;
        this.circuitController = new CircuitController(this.ws.getCircuit());
        this.circuitAnchorPane.getChildren().add(this.circuitController);

        FxmlUtil.scaleWithAnchorPaneParent(workspaceRoot);
    }

    public void addComponent(Component component, int x, int y) {
        this.circuitController.addComponent(component, x, y);
    }

    public void removeComponent(ComponentController component) {
        this.circuitController.removeComponent(component);
    }

}
