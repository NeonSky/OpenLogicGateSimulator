package org.cafebabe.controllers.editor.workspace;

/**
 * Exception thrown when a drag event is
 * triggered on a component other than the
 * one which is currently being dragged.
 */
public class UnexpectedComponentDragException extends RuntimeException {
    private static final long serialVersionUID = -230275300649726915L;

    public UnexpectedComponentDragException(String s) {
        super(s);
    }
}
