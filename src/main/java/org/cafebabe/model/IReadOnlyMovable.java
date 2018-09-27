package org.cafebabe.model;

import java.util.function.Consumer;
import org.cafebabe.model.workspace.Position;

public interface IReadOnlyMovable {
    /* Public */
    void addPositionListener(Consumer<Position> func);

    void removePositionListener(Consumer<Position> listener);

    int getX();

    int getY();
}
