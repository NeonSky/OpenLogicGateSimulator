package org.cafebabe.model.components.connections;

import org.cafebabe.model.IDestructible;
import org.cafebabe.model.IReadOnlyMovable;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;
import org.cafebabe.util.EmptyEvent;

/**
 The common logic for both input- and output ports.
 Ports represent connection points for wires.
 */
public abstract class Port extends LogicStateContainer implements IBelongToModel, IDestructible {
    private IReadOnlyMovable positionTracker = new TrackablePosition(new Position(0, 0));
    private final EmptyEvent onDestroy = new EmptyEvent();


    /* Public */
    public abstract boolean isConnected();

    public void setPositionTracker(IReadOnlyMovable positionTracker) {
        this.positionTracker = positionTracker;
    }

    @Override
    public void destroy() {
        this.onStateChanged.removeListeners();
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    @Override
    public EmptyEvent getOnDestroy() {
        return this.onDestroy;
    }

    /* Package-Private */
    IReadOnlyMovable getPositionTracker() {
        return this.positionTracker;
    }
}
