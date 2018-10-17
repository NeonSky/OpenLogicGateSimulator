package org.cafebabe.view.editor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import org.cafebabe.model.editor.Editor;
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

    @FXML private AnchorPane menuBarAnchorPane;
    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;
    @Getter @FXML private TabPane tabsPane;
    @Getter @FXML private AnchorPane addNewTabButton;

    @Getter private final Editor editor;
    @Getter private final MenuBarView menuBarView;
    private final List<WorkspaceView> workspaceViews = new ArrayList<>();
    private int workspaceCounter;

    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public EditorView(Editor editor) {
        this.editor = editor;
        this.menuBarView = new MenuBarView();

        FxmlUtil.attachFxml(this, "/view/EditorView.fxml");

        this.menuBarAnchorPane.getChildren().add(this.menuBarView);
        FxmlUtil.scaleWithAnchorPaneParent(this.menuBarView);

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

    public void addWorkspaceView(Workspace workspace) {
        WorkspaceView workspaceView = new WorkspaceView(workspace);
        this.workspaceViews.add(workspaceView);
        showWorkspace(this.editor.getWorkspaceList().indexOf(workspace));
        onCreatedSubview.notifyListeners(workspaceView);

        addWorkspaceTab();
    }

    public void removeWorkspace(WorkspaceView workspaceView) {
        this.editor.removeWorkspace(workspaceView.getWorkspace());
        this.workspacesPane.getChildren().remove(workspaceView);
        this.workspaceViews.remove(workspaceView);
    }

    public List<WorkspaceView> getWorkspaceViews() {
        return this.workspaceViews;
    }

    public Tab lastTab() {
        return getTabsPane().getTabs().get(getTabsPane().getTabs().size() - 1);
    }

    public WorkspaceView getCurrentWorkspaceView() {
        return (WorkspaceView)this.workspacesPane.getChildrenUnmodifiable().get(0);
    }

    public Tab getWorkspaceTab(WorkspaceView view) {
        int index = this.workspaceViews.indexOf(view);
        return this.tabsPane.getTabs().get(index);
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
