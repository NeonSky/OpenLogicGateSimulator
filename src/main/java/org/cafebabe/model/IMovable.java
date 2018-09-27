package org.cafebabe.model;

public interface IMovable extends IReadOnlyMovable {
    /* Public */
    void move(int targetX, int targetY);

    void translate(int deltaX, int deltaY);
}
