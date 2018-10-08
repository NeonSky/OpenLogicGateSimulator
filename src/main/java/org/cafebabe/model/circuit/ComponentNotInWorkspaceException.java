package org.cafebabe.model.circuit;

/**
 * Exception thrown when application tries
 * to access invalid components on the workspace.
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
