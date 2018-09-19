package org.cafebabe.model.workspace;

public class Position {
    private int x;
    private int y;

    public Position() {
        this(0, 0);
    }

    public Position(Number x, Number y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void move(int targetX, int targetY) {
        this.x = targetX;
        this.y = targetY;
    }

    public void translate(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
