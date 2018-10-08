package org.cafebabe.model.components.connections;

/**
 * Exception thrown when trying to add a port twice to a single wire.
 */
public class PortAlreadyAddedException extends RuntimeException {

    private static final long serialVersionUID = 6097060014736912805L;

    public PortAlreadyAddedException(String s) {
        super(s);
    }
}