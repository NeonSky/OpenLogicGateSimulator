package org.cafebabe.controllers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;

public class WireController {

    private static Color activeColor = Color.color(234, 38, 38, 1);
    private static Color inactiveColor = Color.color(0, 0, 0, 1);

    private Wire wire;
    private Line wireLine;

    WireController(Wire wire, int startX, int startY, int endX, int endY) {
        this(wire, new Position(startX, startY), new Position(endX, endY));
    }

    WireController(Wire wire, Position startPoint, Position endPoint) {
        this.wire = wire;
        this.wireLine = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        this.wireLine.setFill(getWireColor());
        this.wire.onStateChangedEvent().addListener(this::updateState);
    }

    void moveStartPointTo(int x, int y) {
        this.wireLine.setStartX(x);
        this.wireLine.setStartY(y);
    }

    void moveEndPointTo(int x, int y) {
        this.wireLine.setEndX(x);
        this.wireLine.setEndY(y);
    }

    void translateStartPoint(int deltaX, int deltaY) {
        this.wireLine.setStartX(wireLine.getStartX() + deltaX);
        this.wireLine.setStartY(wireLine.getStartY() + deltaY);
    }

    void translateEndPoint(int deltaX, int deltaY) {
        this.wireLine.setEndX(wireLine.getEndX() + deltaX);
        this.wireLine.setEndY(wireLine.getEndY() + deltaY);
    }

    private Color getWireColor() {
        return (this.wire.isActive()) ? activeColor : inactiveColor;
    }

    public Line getWireLine() {
        return this.wireLine;
    }

    void updateState(Wire wire) {
        this.wireLine.setStroke(getWireColor());
    }
}
