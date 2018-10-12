package org.cafebabe.model.components.connections;

import org.cafebabe.model.IModel;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.IReadOnlyMovable;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;

/**
 The common logic for both input- and output ports.
 Ports represent connection points for wires.
 */
public abstract class Port extends LogicStateContainer implements IModel {
    private IReadOnlyMovable positionTracker = new TrackablePosition(new Position(0, 0));
    private final EmptyEvent onDestroy = new EmptyEvent();


    /* Public */
    public abstract boolean isConnected();

    public void setPositionTracker(IReadOnlyMovable positionTracker) {
        this.positionTracker = positionTracker;
    }

    @Override
    public void destroy() {
        this.onDestroy.notifyListeners();
    }

    @Override
    public EmptyEvent onDestroyed() {
        return this.onDestroy;
    }

    /* Package-Private */
    IReadOnlyMovable getPositionTracker() {
        return this.positionTracker;
    }
}
