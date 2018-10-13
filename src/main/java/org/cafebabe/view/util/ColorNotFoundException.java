package org.cafebabe.view.util;

import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;

/**
 * Exception thrown when a lookup of
 * a color in the application fails.
 */
class ColorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3183483487789084921L;

    ColorNotFoundException(LogicState state) {
        super("No color mapped to state: " + state);
    }
}
