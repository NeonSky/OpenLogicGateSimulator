package org.cafebabe.controllers.editor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.editor.workspace.WorkspaceController;
import org.cafebabe.model.workspace.Workspace;

/**
 * This is the primary view of the program
 * it provides a workspace and a sidebar with components.
 */
public class EditorViewController implements Initializable {

    @FXML private Pane rootPane;
    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;
    @FXML private TabPane tabsPane;

    /* Public */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSidebar();
        initializeWorkspace();
    }

    /* Private */
    private void initializeSidebar() {
        this.sidebarAnchorPane.getChildren().add(new ComponentListController());
    }

    private void initializeWorkspace() {
        this.workspacesPane.getChildren().add(new WorkspaceController(new Workspace()));

        this.workspacesPane.requestLayout();
    }
}
