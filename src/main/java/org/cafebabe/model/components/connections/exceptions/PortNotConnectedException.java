package org.cafebabe.model.components.connections.exceptions;

/**
 * Exception thrown when a wire tries to access a port that is not connected.
 */
public class PortNotConnectedException extends RuntimeException {

    private static final long serialVersionUID = 2420266668389950052L;

    public PortNotConnectedException(String s) {
        super(s);
    }
}