package org.cafebabe.controller.editor;

import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.ISceneController;
import org.cafebabe.controller.editor.componentlist.ComponentListController;
import org.cafebabe.controller.editor.workspace.WorkspaceController;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.EditorView;
import org.cafebabe.view.editor.componentlist.ComponentListView;
import org.cafebabe.view.editor.workspace.WorkspaceView;

/**
 * Handles user interactions with the editor view.
 */
public class EditorController extends Controller implements ISceneController {

    private final EditorView view;


    /* Public */
    public EditorController(EditorView view) {
        super(view);
        this.view = view;

        setSubviewAttachController(WorkspaceView.class, WorkspaceController.class);
        setSubviewAttachController(ComponentListView.class, ComponentListController.class);
        setupEventListeners();

        addNewWorkspace(new Workspace());
        this.view.showWorkspace(0);
        this.view.init();
    }


    /* Public */
    @Override
    public View getView() {
        return this.view;
    }


    /* Private */
    private void setupEventListeners() {
        AnchorPane addNewTabButton = this.view.getAddNewTabButton();
        addNewTabButton.setOnMouseClicked(event -> {
            addNewWorkspace(new Workspace());
            event.consume();
        });

        SingleSelectionModel<Tab> model = this.view.getTabsPane().getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();
        selected.addListener((observable, oldValue, newValue) -> selectWorkspace(newValue));
    }

    private void addNewWorkspace(Workspace workspace) {
        this.view.addNewWorkspace(workspace);

        List<WorkspaceView> workspaceViews = this.view.getWorkspaceViews();
        WorkspaceView newWorkspace = workspaceViews.get(workspaceViews.size() - 1);
        Tab newTab = this.view.lastTab();

        newTab.setOnCloseRequest(event -> removeWorkspace(newWorkspace, newTab));
    }

    private void removeWorkspace(WorkspaceView workspaceView, Tab workspaceTab) {
        this.view.removeWorkspace(workspaceView);

        if (this.view.getWorkspaceViews().isEmpty()) {
            addNewWorkspace(new Workspace());
        }

        if (workspaceTab.isSelected()) {
            selectWorkspace(this.view.getWorkspaceViews().size() - 1);
        }
    }

    private void selectWorkspace(Number index) {
        // JavaFX can sometimes return an out-of-bounds index
        int i = index.intValue();
        if (i < 0 || this.view.getWorkspaceViews().size() <= i) {
            return;
        }

        this.view.showWorkspace(i);
    }
}
