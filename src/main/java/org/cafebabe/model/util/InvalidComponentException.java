package org.cafebabe.model.util;

/**
 * Exception thrown when application
 * tries to access or instantiate
 * an invalid component.
 */
public class InvalidComponentException extends RuntimeException {

    private static final long serialVersionUID = 3303160542107150549L;

    public InvalidComponentException(String s) {
        super(s);
    }
}
