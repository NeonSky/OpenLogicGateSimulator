package org.cafebabe.controllers.editor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
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
    private final List<WorkspaceController> workspaces = new ArrayList<>();

    /* Public */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSidebar();
        initializeWorkspace();
        initializeTabPane();
    }

    public void addNewWorkspace() {
        Workspace workspace = new Workspace();
        WorkspaceController workspaceController = new WorkspaceController(workspace);
        this.workspaces.add(workspaceController);
        addWorkspaceTab(workspaceController);
    }

    public void removeWorkspace(Event event, WorkspaceController workspaceController) {
        event.consume();
        this.workspacesPane.getChildren().remove(workspaceController);
        this.workspaces.remove(workspaceController);

        if (this.workspaces.isEmpty()) {
            addNewWorkspace();
        }
    }

    /* Private */
    private void initializeSidebar() {
        this.sidebarAnchorPane.getChildren().add(new ComponentListController());
    }

    private void initializeWorkspace() {
        addNewWorkspace();
        this.workspacesPane.getChildren().addAll(this.workspaces.get(0));
        this.workspacesPane.requestLayout();
    }

    private void initializeTabPane() {
        SingleSelectionModel<Tab> model = this.tabsPane.getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();
        selected.addListener((observable, oldValue, newValue) -> selectWorkspace(newValue));
    }

    private void selectWorkspace(Number index) {
        if (index.intValue() < 0) {
            return;
        }

        this.workspacesPane.getChildren().clear();
        WorkspaceController selectedWorkspace = this.workspaces.get(index.intValue());
        this.workspacesPane.getChildren().add(selectedWorkspace);
    }

    private void addWorkspaceTab(WorkspaceController workspaceController) {
        Tab tab = new Tab();
        tab.setText("New Workspace");
        tab.setClosable(true);
        tab.setOnClosed(event -> this.removeWorkspace(event, workspaceController));
        this.tabsPane.getTabs().add(tab);
        this.tabsPane.getSelectionModel().select(tab);
    }
}
