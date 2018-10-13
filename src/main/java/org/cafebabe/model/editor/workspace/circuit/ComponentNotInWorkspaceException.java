package org.cafebabe.model.editor.workspace.circuit;

/**
 * Exception thrown when application tries
 * to access invalid component on the workspace.
 */
public class ComponentNotInWorkspaceException extends RuntimeException {

    private static final long serialVersionUID = -7031747690273410366L;

    public ComponentNotInWorkspaceException() {
        super();
    }

    public ComponentNotInWorkspaceException(String s) {
        super(s);
    }
}
