package org.cafebabe.controllers;

import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;

public class WireController {

    private Wire wire;
    private Position startPoint;
    private Position endPoint;

    WireController(Wire wire, int startX, int startY, int endX, int endY) {
        this(wire, new Position(startX, startY), new Position(endX, endY));
    }

    WireController(Wire wire, Position startPoint, Position endPoint) {
        this.wire = wire;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    void moveStartPointTo(int x, int y) {
        this.startPoint.move(x, y);
    }

    void moveEndPointTo(int x, int y) {
        this.endPoint.move(x, y);
    }

    void translateStartPoint(int deltaX, int deltaY) {
        this.startPoint.translate(deltaX, deltaY);
    }

    void translateEndPoint(int deltaX, int deltaY) {
        this.endPoint.translate(deltaX, deltaY);
    }
}
