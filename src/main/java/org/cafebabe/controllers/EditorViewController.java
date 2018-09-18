package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.FlowPane;
import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.MockComponent;
import org.cafebabe.model.workspace.Workspace;

import java.util.ResourceBundle;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;
    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;

    private WorkspaceController workspaceController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSidebar();
        initializeWorkspace();
    }

    private void initializeWorkspace() {
        this.workspaceController = new WorkspaceController(new Workspace());
        this.workspacesPane.getChildren().add(this.workspaceController);

        workspacesPane.requestLayout();
    }

    private void initializeSidebar() {
        this.sidebarAnchorPane.getChildren().add(new ComponentListController());
    }
}
