package org.cafebabe.controller.editor.workspace.circuit;

/**
 * Exception thrown when a drag event is
 * triggered on a component other than the
 * one which is currently being dragged.
 */
class UnexpectedComponentDragException extends RuntimeException {
    private static final long serialVersionUID = -230275300649726915L;

    UnexpectedComponentDragException(String s) {
        super(s);
    }
}
