package org.cafebabe.gui.editor.workspace;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.gui.util.FxmlUtil;

/**
 * Workspace visual.
 */
class WorkspaceView extends AnchorPane {

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    WorkspaceView(CircuitController circuitController) {
        FxmlUtil.attachFxml(this, "/view/WorkspaceView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this.workspaceRoot);

        this.circuitAnchorPane.getChildren().add(circuitController);
    }
}
