package org.cafebabe.model.editor.workspace.circuit;

/**
 * Exception thrown when application tries
 * to add the same wire to a circuit more than once.
 */
public class WireAlreadyAddedException extends RuntimeException {

    private static final long serialVersionUID = -7294478223493838073L;

    public WireAlreadyAddedException() {
        super();
    }

    public WireAlreadyAddedException(String s) {
        super(s);
    }
}
