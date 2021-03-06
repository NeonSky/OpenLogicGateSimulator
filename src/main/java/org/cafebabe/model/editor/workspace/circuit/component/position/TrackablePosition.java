package org.cafebabe.model.editor.workspace.circuit.component.position;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import lombok.Getter;
import org.cafebabe.model.IModel;
import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;

/**
 * A Mutable position with an event that triggers every time it has been moved.
 */
public class TrackablePosition extends Position implements IMovable, IModel {
    @Getter private final EmptyEvent onDestroy = new EmptyEvent();
    private final Event<Position> onPositionChanged = new Event<>();
    private final Set<TrackablePosition> clones = new HashSet<>();
    private int x;
    private int y;
    private int xOffset;
    private int yOffset;

    public TrackablePosition(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    @SuppressWarnings("checkstyle:parametername")
    private TrackablePosition(TrackablePosition pos, int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        pos.addPositionListener(p -> {
            this.x = p.getX();
            this.y = p.getY();
            this.notifyPositionChanged();
        });
    }

    /* Public */
    @SuppressWarnings("checkstyle:parametername")
    public IReadOnlyMovable offsetClone(int xOffset, int yOffset) {
        TrackablePosition pos = new TrackablePosition(this, xOffset, yOffset);
        this.clones.add(pos);
        return pos;
    }

    @Override
    public void addPositionListener(Consumer<Position> func) {
        this.onPositionChanged.addListenerWithOwner(func, this);
    }

    @Override
    public void removePositionListeners() {
        this.onPositionChanged.removeListenersWithOwner(this);
    }

    @Override
    public void destroy() {
        this.clones.forEach(TrackablePosition::destroy);
        this.onPositionChanged.removeListeners();
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    @Override
    public void move(int targetX, int targetY) {
        this.x = targetX;
        this.y = targetY;
        notifyPositionChanged();
    }

    @Override
    public void translate(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
        notifyPositionChanged();
    }

    @Override
    public int getX() {
        return this.x + this.xOffset;
    }

    @Override
    public int getY() {
        return this.y + this.yOffset;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    /* Private */
    private void notifyPositionChanged() {
        int x = this.getX();
        int y = this.getY();
        this.onPositionChanged.notifyListeners(new Position(x, y));
        this.clones.forEach(p -> p.move(x, y));
    }
}
