package org.cafebabe.viewmodel;

/**
 * Exception thrown when application encounters
 * an unexpected subclass of Port.
 */
public class InvalidPortTypeException extends RuntimeException {
    private static final long serialVersionUID = 4419338370651502153L;

    public InvalidPortTypeException() {
        super();
    }

    public InvalidPortTypeException(String s) {
        super(s);
    }
}
