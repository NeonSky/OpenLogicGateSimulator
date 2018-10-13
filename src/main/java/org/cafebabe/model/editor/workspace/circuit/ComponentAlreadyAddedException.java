package org.cafebabe.model.editor.workspace.circuit;

/**
 * Exception thrown when a component
 * can't be added to the workspace because it's
 * already there.
 */
public class ComponentAlreadyAddedException extends RuntimeException {

    private static final long serialVersionUID = 7436277057618436546L;

    public ComponentAlreadyAddedException() {
        super();
    }

    public ComponentAlreadyAddedException(String s) {
        super(s);
    }
}
