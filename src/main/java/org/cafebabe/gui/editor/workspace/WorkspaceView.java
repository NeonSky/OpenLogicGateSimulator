package org.cafebabe.gui.editor.workspace;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.gui.IView;
import org.cafebabe.gui.editor.workspace.circuit.CircuitController;
import org.cafebabe.gui.util.FxmlUtil;

/**
 * Workspace visual.
 */
class WorkspaceView extends AnchorPane implements IView {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    WorkspaceView(CircuitController circuitController) {
        FxmlUtil.attachFxml(this, "/view/WorkspaceView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this.workspaceRoot);

        this.circuitAnchorPane.getChildren().add(circuitController.getView());
    }

    /* Public */
    @Override
    public void destroy() {
        FxmlUtil.destroy(this);
    }
}
