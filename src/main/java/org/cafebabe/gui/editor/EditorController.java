package org.cafebabe.gui.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.cafebabe.gui.IController;
import org.cafebabe.gui.editor.workspace.WorkspaceController;
import org.cafebabe.model.workspace.Workspace;

/**
 * This is the primary view of the editor.
 * it provides a set of workspaces and a sidebar with components.
 */
public class EditorController implements IController {

    private final EditorView view = new EditorView();
    private final List<WorkspaceController> workspaces = new ArrayList<>();


    /* Public */
    public EditorController() {
        this.view.onNewWorkspaceButton.addListener(this::addNewWorkspace);
        this.view.onSelectTab.addListener(this::selectWorkspace);

        addNewWorkspace();
        this.view.showWorkspace(this.workspaces.get(0));
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @Override
    public void destroy() {
        this.workspaces.forEach(IController::destroy);
        this.view.destroy();
    }

    /* Private */
    private void addNewWorkspace() {
        Workspace workspace = new Workspace();
        WorkspaceController workspaceController = new WorkspaceController(workspace);
        this.workspaces.add(workspaceController);

        Tab tab = this.view.addWorkspaceTab();
        tab.setOnCloseRequest(event -> {
            this.removeWorkspace(workspaceController);
            if (tab.isSelected()) {
                selectWorkspace(this.workspaces.size() - 1);
            }
        });
    }

    private void removeWorkspace(WorkspaceController workspaceController) {
        this.view.removeWorkspace(workspaceController);
        this.workspaces.remove(workspaceController);

        if (this.workspaces.isEmpty()) {
            addNewWorkspace();
        }
    }

    private void selectWorkspace(Number index) {
        // JavaFX can sometimes return an out of bounds index
        if (index.intValue() < 0 || this.workspaces.size() <= index.intValue()) {
            return;
        }

        WorkspaceController selectedWorkspace = this.workspaces.get(index.intValue());
        this.view.showWorkspace(selectedWorkspace);
    }
}
