package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Workspace;

class WorkspaceController extends AnchorPane {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    private final Workspace ws;
    private final CircuitController circuitController;

    WorkspaceController(Workspace ws) {
        FxmlUtil.attachFXML(this, "/view/WorkspaceView.fxml");

        this.ws = ws;
        this.circuitController = new CircuitController(this.ws.getCircuit());
        this.circuitAnchorPane.getChildren().add(this.circuitController);

        setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
            System.out.println("workspace controller drag over");
        });

        FxmlUtil.scaleWithAnchorPaneParent(workspaceRoot);
    }

    void addComponent(Component component, int x, int y) {
        this.circuitController.addComponent(component, x, y);
    }

    void removeComponent(ComponentController component) {
        this.circuitController.removeComponent(component);
    }

}
