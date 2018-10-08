package org.cafebabe.util;

import org.cafebabe.model.components.connections.LogicState;

/**
 * Exception thrown when a lookup of
 * a color in the application fails.
 */
public class ColorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3183483487789084921L;

    public ColorNotFoundException(LogicState state) {
        super("No color mapped to state: " + state);
    }
}
