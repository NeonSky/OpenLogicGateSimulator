package org.cafebabe.controllers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import org.cafebabe.model.circuit.IBelongToCircuit;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.LogicStateContainer;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.ColorUtil;
import org.cafebabe.util.Event;

public class WireController implements ISelectable {


    private final Event<WireController> willBeDestroyed = new Event<>();

    private static final int WIRE_WIDTH = 6;

    private boolean isSelected;
    private Wire wire;
    private CubicCurve wireLine;
    private PositionTracker startPointTracker = PositionTracker.trackNothing;
    private PositionTracker endPointTracker = PositionTracker.trackNothing;

    WireController(Wire wire, Position startPoint, Position endPoint) {
        this.wire = wire;
        this.wireLine = new CubicCurve();
        moveLineTo(startPoint, endPoint);
        setWireDrawingOptions();
        this.wire.onStateChangedEvent().addListener(this::updateVisualState);
        this.wire.onWillBeDestroyed().addListener(this::onDestroy);
        this.wireLine.setPickOnBounds(false);
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
        LogicState currentState = this.wire.logicState();
        return (this.isSelected) ? ColorUtil.SELECTED : ColorUtil.getStateColor(currentState);
    }

    private void setWireDrawingOptions() {
        this.wireLine.setStroke(this.getWireColor());
        this.wireLine.setFill(Color.TRANSPARENT);
        this.wireLine.setStrokeWidth(WIRE_WIDTH);
        this.wireLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Effect dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 10, 0.2, 0, 0);
        this.wireLine.setEffect(dropShadow);
    }

    private void updateVisualState(LogicStateContainer wire) {
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

    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.wireLine.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
    }

    public void removeClickListener(EventHandler<MouseEvent> listener) {
        this.wireLine.removeEventFilter(MouseEvent.MOUSE_CLICKED, listener);
    }

    public Node getWireLine() {
        return this.wireLine;
    }

    public Wire getWire() {
        return this.wire;
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

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void select() {
        setSelected(true);
        updateVisualState(this.wire);
    }

    @Override
    public void deselect() {
        setSelected(false);
        updateVisualState(this.wire);
    }

    @Override
    public void disconnectFromWorkspace() {
        this.wire.onStateChangedEvent().removeListener(this::updateVisualState);
        this.wire.disconnectAll();
        this.startPointTracker = null;
        this.endPointTracker = null;
    }

    private void onDestroy(Wire w) {
        disconnectFromWorkspace();
        willBeDestroyed.notifyListeners(this);
    }

    public Event<WireController> onWillBeDestroyed() {
        return willBeDestroyed;
    }

    @Override
    public IBelongToCircuit getModelObject() {
        return this.wire;
    }
}
