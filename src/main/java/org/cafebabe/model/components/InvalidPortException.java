package org.cafebabe.model.components;

/**
 * Exception thrown when application
 * tries to access or instantiate an
 * invalid port.
 */
public class InvalidPortException extends RuntimeException {

    private static final long serialVersionUID = 3040451034172114935L;

    public InvalidPortException(String s) {
        super(s);
    }
}
