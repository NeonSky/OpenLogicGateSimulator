package org.cafebabe.model.editor.workspace.circuit.component.position;

import org.cafebabe.model.editor.util.IReadOnlyMovable;

public interface IMovable extends IReadOnlyMovable {
    void move(int targetX, int targetY);
    void translate(int deltaX, int deltaY);
}
