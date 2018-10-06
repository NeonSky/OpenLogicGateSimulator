package org.cafebabe.model.editor.workspace.circuit.component.connection;

import org.cafebabe.model.IModel;
import org.cafebabe.model.editor.util.EmptyEvent;
import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.TrackablePosition;
import org.cafebabe.model.util.IdGenerator;

/**
 The common logic for both input- and output ports.
 Ports represent connection points for wires.
 */
public abstract class Port extends LogicStateContainer implements IModel {
    private IReadOnlyMovable positionTracker = new TrackablePosition(new Position(0, 0));
    private final EmptyEvent onDestroy = new EmptyEvent();
    private boolean destructionPending;
    private final int id = IdGenerator.getNewId();


    /* Public */
    public abstract boolean isConnected();

    public void setPositionTracker(IReadOnlyMovable positionTracker) {
        this.positionTracker = positionTracker;
    }

    @Override
    public void destroy() {
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.onDestroy.notifyListeners();
        }
    }

    @Override
    public EmptyEvent getOnDestroy() {
        return this.onDestroy;
    }

    /* Package-Private */
    IReadOnlyMovable getPositionTracker() {
        return this.positionTracker;
    }

    public int getId() {
        return this.id;
    }
}
