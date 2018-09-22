package org.cafebabe.controllers;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;

import java.util.Map;

public class WireController {

    private static final Color ACTIVE_COLOR = Color.color(234.0/255, 38.0/255, 38.0/255, 1);
    private static final Color INACTIVE_COLOR = Color.color(0, 0, 0, 1);
    private static final Color UNDEFINED_COLOR = Color.color(0.8, 0.8, 0, 1);
    private static final int WIRE_WIDTH = 4;

    private static final Map<LogicState, Color> STATE_TO_COLOR = Map.ofEntries(
        Map.entry(LogicState.HIGH, ACTIVE_COLOR),
        Map.entry(LogicState.LOW, INACTIVE_COLOR),
        Map.entry(LogicState.UNDEFINED, UNDEFINED_COLOR)
    );

    private Wire wire;
    private CubicCurve wireLine;
    private PositionTracker startPointTracker = PositionTracker.trackNothing;
    private PositionTracker endPointTracker = PositionTracker.trackNothing;


    WireController(Wire wire, Position startPoint, Position endPoint) {
        this.wire = wire;
        this.wireLine = new CubicCurve();
        moveLineTo(startPoint, endPoint);
        setWireDrawingOptions();
        this.wire.onStateChangedEvent().addListener((w) -> this.updateState());
    }

    WireController(Wire wire, int startX, int startY, int endX, int endY) {
        this(wire, new Position(startX, startY), new Position(endX, endY));
    }


    private void moveStartPointTo(Number x, Number y) {
        this.wireLine.setStartX(x.doubleValue());
        this.wireLine.setStartY(y.doubleValue());
        setWireControlPoints();
    }

    private void moveEndPointTo(Number x, Number y) {
        this.wireLine.setEndX(x.doubleValue());
        this.wireLine.setEndY(y.doubleValue());
        setWireControlPoints();
    }

    private Color getWireColor() {
        return STATE_TO_COLOR.get(this.wire.logicState());
    }

    private void setWireDrawingOptions() {
        this.wireLine.setStroke(this.getWireColor());
        this.wireLine.setFill(Color.TRANSPARENT);
        this.wireLine.setStrokeWidth(WIRE_WIDTH);
        this.wireLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Effect dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 10, 0.2, 0, 0);
        this.wireLine.setEffect(dropShadow);
    }

    private void updateState() {
        this.wireLine.setStroke(getWireColor());
    }

    private void setWireControlPoints() {
        double startX = this.wireLine.getStartX();
        double startY = this.wireLine.getStartY();
        double endX = this.wireLine.getEndX();
        double endY = this.wireLine.getEndY();
        this.wireLine.setControlX1((endX + startX) / 2);
        this.wireLine.setControlY1(startY);
        this.wireLine.setControlX2((endX + startX) / 2);
        this.wireLine.setControlY2(endY);
    }


    public Node getWireLine() {
        return this.wireLine;
    }

    public void moveLineTo(Position startPoint, Position endPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    public void moveStartPointTo(Position startPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
    }

    public void moveEndPointTo(Position endPoint) {
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    public void updatePosition() {
        moveStartPointTo(startPointTracker.getCurrentPosition());
        moveEndPointTo(endPointTracker.getCurrentPosition());
    }

    public void bindStartPointTo(PositionTracker startPointTracker) {
        this.startPointTracker = startPointTracker;
        this.updatePosition();
    }

    public void bindEndPointTo(PositionTracker endPointTracker) {
        this.endPointTracker = endPointTracker;
        this.updatePosition();
    }
}
