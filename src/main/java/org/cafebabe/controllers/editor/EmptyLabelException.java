package org.cafebabe.controllers.editor;

/**
 * Exception thrown when application tries to render an empty label.
 */
public class EmptyLabelException extends RuntimeException {
    private static final long serialVersionUID = 2607349537404232540L;

    public EmptyLabelException(String s) {
        super(s);
    }
}
