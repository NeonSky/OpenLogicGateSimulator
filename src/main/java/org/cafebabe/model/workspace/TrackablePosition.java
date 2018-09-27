package org.cafebabe.model.workspace;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.cafebabe.model.IMovable;
import org.cafebabe.model.IReadOnlyMovable;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class TrackablePosition extends Position implements IMovable {
    private final EmptyEvent onDestroy = new EmptyEvent();
    private final Event<Position> onPositionChanged = new Event<>();
    private final Set<TrackablePosition> clones = new HashSet<>();
    private int x;
    private int y;
    private int xOffset = 0;
    private int yOffset = 0;

    public TrackablePosition(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

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
    public IReadOnlyMovable offsetClone(int xOffset, int yOffset) {
        TrackablePosition pos = new TrackablePosition(this, xOffset, yOffset);
        clones.add(pos);
        return pos;
    }

    @Override
    public void addPositionListener(Consumer<Position> func) {
        onPositionChanged.addListener(func);
    }

    @Override
    public void removePositionListener(Consumer<Position> listener) {
        onPositionChanged.removeListener(listener);
    }

    public void destroy() {
        this.clones.forEach(TrackablePosition::destroy);
        this.onPositionChanged.removeListeners();
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
    }

    public EmptyEvent getOnDestroy() {
        return onDestroy;
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

    /* Private */
    private void notifyPositionChanged() {
        int x = this.getX();
        int y = this.getY();
        onPositionChanged.notifyListeners(new Position(x, y));
        clones.forEach(p -> p.move(x, y));
    }
}
