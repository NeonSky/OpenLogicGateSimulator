package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.FlowPane;
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
        sidebarAnchorPane.getChildren().add(new ComponentListController());

        this.workspaceController = new WorkspaceController(new Workspace());
        this.workspacesPane.getChildren().add(this.workspaceController);

        MockComponent mockComponent1 = new MockComponent();
        MockComponent mockComponent2 = new MockComponent();
        MockComponent mockComponent3 = new MockComponent();
        MockComponent mockComponent4 = new MockComponent();

        this.workspaceController.addComponent(mockComponent1, 0, 100);
        this.workspaceController.addComponent(mockComponent2, 100, 100);
        this.workspaceController.addComponent(mockComponent3, 100, 0);
        this.workspaceController.addComponent(mockComponent4, 0, 0);

        workspacesPane.requestLayout();
    }
}
