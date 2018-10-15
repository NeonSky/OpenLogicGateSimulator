package org.cafebabe.view.editor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.componentlist.ComponentListView;
import org.cafebabe.view.editor.workspace.WorkspaceView;
import org.cafebabe.view.util.FxmlUtil;


/**
 * This is the primary view of the editor.
 * it provides a set of workspaces and a sidebar with components.
 */
public class EditorView extends View {

    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;
    @FXML private TabPane tabsPane;
    @FXML private AnchorPane addNewTabButton;

    private final List<WorkspaceView> workspaceViews = new ArrayList<>();
    private int workspaceCounter;

    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public EditorView() {
        FxmlUtil.attachFxml(this, "/view/EditorView.fxml");

        this.workspacesPane.requestLayout();
    }

    /* Public */
    @Override
    public void init() {
        addSubview(this.sidebarAnchorPane, new ComponentListView());
    }

    public void showWorkspace(int index) {
        this.workspacesPane.getChildren().clear();
        this.workspacesPane.getChildren().add(this.workspaceViews.get(index));
    }

    public void addNewWorkspace(Workspace workspace) {
        WorkspaceView workspaceView = new WorkspaceView(workspace);
        onCreatedSubview.notifyListeners(workspaceView);
        this.workspaceViews.add(workspaceView);

        addWorkspaceTab();
    }

    public void removeWorkspace(WorkspaceView workspaceView) {
        this.workspacesPane.getChildren().remove(workspaceView);
        this.workspaceViews.remove(workspaceView);
    }

    public List<WorkspaceView> getWorkspaceViews() {
        return this.workspaceViews;
    }

    public WorkspaceView getCurrentWorkspaceView() {
        return (WorkspaceView)this.workspacesPane.getChildrenUnmodifiable().get(0);
    }

    public TabPane getTabsPane() {
        return this.tabsPane;
    }

    public Tab lastTab() {
        return getTabsPane().getTabs().get(getTabsPane().getTabs().size() - 1);
    }

    public AnchorPane getAddNewTabButton() {
        return this.addNewTabButton;
    }


    /* Private */
    private void addWorkspaceTab() {
        Tab tab = new Tab();
        tab.setText("Workspace " + this.workspaceCounter++);
        tab.setClosable(true);
        tab.setOnClosed(javafx.event.Event::consume);
        this.tabsPane.getTabs().add(tab);
        this.tabsPane.getSelectionModel().select(tab);
    }
}
