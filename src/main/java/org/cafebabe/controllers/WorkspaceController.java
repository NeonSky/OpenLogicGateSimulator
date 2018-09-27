package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.editor.workspace.CircuitController;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.viewmodel.ViewModel;

public class WorkspaceController extends AnchorPane {

    @FXML
    private AnchorPane workspaceRoot;
    @FXML
    private AnchorPane circuitAnchorPane;

    public WorkspaceController(Workspace workspace) {
        FxmlUtil.attachFXML(this, "/view/WorkspaceView.fxml");

        Workspace workspace1 = workspace;
        ViewModel viewModel = new ViewModel(workspace1);
        CircuitController circuitController = new CircuitController(viewModel);
        this.circuitAnchorPane.getChildren().add(circuitController);

        FxmlUtil.scaleWithAnchorPaneParent(workspaceRoot);
    }
}
