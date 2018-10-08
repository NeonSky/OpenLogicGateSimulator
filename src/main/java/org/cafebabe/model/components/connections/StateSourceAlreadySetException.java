package org.cafebabe.model.components.connections;

/**
 * Exception thrown when trying to set the state source of an input port twice.
 */
public class StateSourceAlreadySetException extends RuntimeException {

    private static final long serialVersionUID = -1965422088421164237L;

    public StateSourceAlreadySetException(String s) {
        super(s);
    }
}
