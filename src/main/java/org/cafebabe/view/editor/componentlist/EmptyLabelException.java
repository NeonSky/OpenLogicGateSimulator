package org.cafebabe.view.editor.componentlist;

/**
 * Exception thrown when application tries to render an empty label.
 */
class EmptyLabelException extends RuntimeException {
    private static final long serialVersionUID = 2607349537404232540L;

    EmptyLabelException(String s) {
        super(s);
    }
}
