package org.cafebabe.model.editor;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.storage.ISaveLoadWorkspaces;
import org.cafebabe.model.storage.JsonStorage;

/**
 * Keeps track of workspaces in the model, and provides methods to save the
 * current workspace and load a new workspace.
 */
public class Editor {

    @Getter private final List<Workspace> workspaceList = new ArrayList<>();
    private final ISaveLoadWorkspaces storageHandler = new JsonStorage();
    @Getter private Workspace currentWorkspace;

    public void createNewWorkspace() {
        Workspace workspace = new Workspace();
        this.currentWorkspace = workspace;
        this.workspaceList.add(workspace);
    }

    public void removeWorkspace(Workspace workspace) {
        if (!this.workspaceList.contains(workspace)) {
            throw new WorkspaceNotInEditorException();
        }

        this.workspaceList.remove(workspace);
    }

    public void switchWorkspace(int index) {
        this.currentWorkspace = this.workspaceList.get(index);
    }

    public void saveCurrentWorkspace() {
        try {
            this.storageHandler.saveWorkspace(this.currentWorkspace, "savefile.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Workspace loadDummyWorkspace() {
        try {
            Workspace workspace = this.storageHandler.loadWorkspace("savefile.json");
            this.workspaceList.add(workspace);
            return workspace;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
