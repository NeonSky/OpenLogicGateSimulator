package org.cafebabe.model.editor.util;

import java.util.function.Consumer;
import org.cafebabe.model.editor.workspace.Position;

public interface IReadOnlyMovable {
    void addPositionListener(Consumer<Position> func);
    void removePositionListener(Consumer<Position> listener);
    int getX();
    int getY();
}
