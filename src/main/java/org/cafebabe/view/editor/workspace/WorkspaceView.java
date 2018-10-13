package org.cafebabe.view.editor.workspace;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.CircuitView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Provides a pannable, zoomable workspace in which a circuit can be constructed.
 */
public class WorkspaceView extends View {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    //private final Workspace workspace;
    private final ViewModel viewModel;


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public WorkspaceView(Workspace workspace) {
        //this.workspace = workspace;
        this.viewModel = new ViewModel(workspace);

        FxmlUtil.attachFxml(this, "/view/WorkspaceView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this.workspaceRoot);
    }

    @Override
    public void init() {
        addSubview(this.circuitAnchorPane, new CircuitView(new Circuit(), this.viewModel));
    }

}
