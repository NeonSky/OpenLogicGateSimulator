package org.cafebabe.controllers;

import org.cafebabe.model.workspace.Position;

import java.util.concurrent.Callable;

public class PositionTracker {
    private final Callable<Position> calculatePosition;
    public final static PositionTracker trackNothing = new PositionTracker(()->new Position(0, 0));

    public PositionTracker(Callable<Position> calculatePosition) {
        this.calculatePosition = calculatePosition;
    }

    public Position getCurrentPosition() {
        try {
            if(calculatePosition != null) {
                return calculatePosition.call();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Position(0, 0);
    }
}
