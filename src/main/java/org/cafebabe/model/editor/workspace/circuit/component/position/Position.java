package org.cafebabe.model.editor.workspace.circuit.component.position;

import lombok.Getter;

/**
 * An Immutable 2D Vector.
 */
public class Position {
    @Getter private final int x;
    @Getter private final int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Public */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
