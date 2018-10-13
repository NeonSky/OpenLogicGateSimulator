package org.cafebabe.model.editor.workspace;

import org.cafebabe.model.editor.util.IReadOnlyMovable;

public interface IMovable extends IReadOnlyMovable {
    /* Public */
    void move(int targetX, int targetY);

    void translate(int deltaX, int deltaY);
}
