package org.cafebabe.model.editor;

/**
 * Exception thrown when application tries to remove Workspace
 * not currently found in editor.
 */
public class WorkspaceNotInEditorException extends RuntimeException {

    private static final long serialVersionUID = 130123872198436132L;

    public WorkspaceNotInEditorException() {
        super();
    }

    public WorkspaceNotInEditorException(String s) {
        super(s);
    }
}
