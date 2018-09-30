package org.cafebabe.controllers.editor.workspace;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.viewmodel.ViewModel;

public class WorkspaceController extends AnchorPane {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    public WorkspaceController(Workspace workspace) {
        FxmlUtil.attachFxml(this, "/view/WorkspaceView.fxml");

        Workspace workspace1 = workspace;
        ViewModel viewModel = new ViewModel(workspace1);
        CircuitController circuitController = new CircuitController(viewModel);
        setupFxml(circuitController);
    }

    private void setupFxml(CircuitController circuitController) {
        this.circuitAnchorPane.getChildren().add(circuitController);

        FxmlUtil.scaleWithAnchorPaneParent(this.workspaceRoot);
    }
}
