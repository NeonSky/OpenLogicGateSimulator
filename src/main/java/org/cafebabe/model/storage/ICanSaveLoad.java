package org.cafebabe.model.storage;

import org.cafebabe.model.editor.workspace.Workspace;

public interface ICanSaveLoad {
    void saveWorkspace(Workspace workspace, String location);
    Workspace loadWorkspace(String location);
}
