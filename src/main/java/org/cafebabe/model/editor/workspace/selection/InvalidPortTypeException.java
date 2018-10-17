package org.cafebabe.model.editor.workspace.selection;

/**
 * Exception thrown when application encounters
 * an unexpected subclass of Port.
 */
public class InvalidPortTypeException extends RuntimeException {
    private static final long serialVersionUID = 4419338370651502153L;

    public InvalidPortTypeException() {
        super();
    }
}
