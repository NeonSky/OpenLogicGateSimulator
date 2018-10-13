package org.cafebabe.model.editor.util;

/**
 * Exception thrown when application
 * tries to access or instantiate
 * an invalid component.
 */
class InvalidComponentException extends RuntimeException {

    private static final long serialVersionUID = 3303160542107150549L;

    InvalidComponentException(String s) {
        super(s);
    }
}
