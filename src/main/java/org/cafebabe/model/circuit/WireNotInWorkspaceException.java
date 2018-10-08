package org.cafebabe.model.circuit;

/**
 * Exception thrown when application tries
 * to access a wire which is not in the
 * current workspace.
 */
public class WireNotInWorkspaceException extends RuntimeException {
    private static final long serialVersionUID = 5151647616225573646L;

    public WireNotInWorkspaceException() {
        super();
    }

    public WireNotInWorkspaceException(String s) {
        super(s);
    }
}
