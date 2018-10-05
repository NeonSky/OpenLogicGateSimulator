package org.cafebabe.model.components.connections;

import org.cafebabe.model.IDestructible;
import org.cafebabe.model.IReadOnlyMovable;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;

/**
 The common logic for both input- and output ports.
 Ports represent connection points for wires.
 */
public abstract class Port extends LogicStateContainer implements IBelongToModel, IDestructible {
    private IReadOnlyMovable positionTracker = new TrackablePosition(new Position(0, 0));

    /* Public */
    public abstract boolean isConnected();

    public IReadOnlyMovable getPositionTracker() {
        return this.positionTracker;
    }

    public void setPositionTracker(IReadOnlyMovable positionTracker) {
        this.positionTracker = positionTracker;
    }
}
