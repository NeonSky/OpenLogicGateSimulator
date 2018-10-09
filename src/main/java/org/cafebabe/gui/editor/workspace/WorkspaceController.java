package org.cafebabe.gui.editor.workspace;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.viewmodel.ViewModel;

/**
 * Provides a pannable, zoomable workspace in which a circuit can be constructed.
 */
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
