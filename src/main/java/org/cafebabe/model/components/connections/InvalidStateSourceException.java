package org.cafebabe.model.components.connections;

/**
 * Exception thrown when application tries
 * to set an invalid state source to
 * an input port.
 */
public class InvalidStateSourceException extends RuntimeException {

    private static final long serialVersionUID = -562094582310083881L;

    public InvalidStateSourceException(String s) {
        super(s);
    }
}
