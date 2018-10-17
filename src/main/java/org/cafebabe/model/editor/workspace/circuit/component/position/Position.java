package org.cafebabe.model.editor.workspace.circuit.component.position;

/**
 * An Immutable 2D Vector.
 */
public class Position {
    private final int x;
    private final int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Public */
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
