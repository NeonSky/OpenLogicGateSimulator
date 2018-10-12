package org.cafebabe.gui.editor.workspace.circuit.wire;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Transform;
import org.cafebabe.gui.IView;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.util.IReadOnlyMovable;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.ColorUtil;


/**
 * Wire visual.
 */
class WireView implements IView {

    private static final int WIRE_WIDTH = 6;
    private final CubicCurve wireLine;
    private final Wire wire;
    private boolean isSelected;


    WireView(Wire wire) {
        this.wire = wire;
        this.wireLine = new CubicCurve();
        this.wireLine.getTransforms().add(0,Transform.scale(1,1));
        this.wireLine.setPickOnBounds(false);
        setInitialWirePoints();
        stylizeWireLine();
    }

    /* Public */
    @Override
    public void destroy() {
        FxmlUtil.destroy(this.wireLine);
    }

    /* Package-Private */
    void addClickListener(EventHandler<MouseEvent> listener) {
        this.wireLine.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
    }

    Color getWireColor() {
        LogicState currentState = this.wire.logicState();
        return this.isSelected ? ColorUtil.SELECTED : ColorUtil.getStateColor(currentState);
    }

    void moveStartPointTo(Position startPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
    }

    void moveStartPointTo(Number x, Number y) {
        this.wireLine.setStartX(x.doubleValue());
        this.wireLine.setStartY(y.doubleValue());
        updateWireLineTransform();
    }

    void moveEndPointTo(Position endPoint) {
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    void moveEndPointTo(Number x, Number y) {
        this.wireLine.setEndX(x.doubleValue());
        this.wireLine.setEndY(y.doubleValue());
        updateWireLineTransform();
    }

    void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    void updateVisualState() {
        this.wireLine.setStroke(getWireColor());
        this.wireLine.setVisible(this.wire.isAnyOutputConnected()
                && this.wire.isAnyInputConnected());
    }

    void setTransform(Transform transform) {
        this.wireLine.getTransforms().set(0, transform);
    }

    Node getFxView() {
        return this.wireLine;
    }


    /* Private */
    private void setInitialWirePoints() {
        IReadOnlyMovable startPos = this.wire.getStartPos();
        IReadOnlyMovable endPos = this.wire.getEndPos();
        moveStartPointTo(startPos.getX(), startPos.getY());
        moveEndPointTo(endPos.getX(), endPos.getY());
    }

    private void stylizeWireLine() {
        this.wireLine.setStroke(this.getWireColor());
        this.wireLine.setFill(Color.TRANSPARENT);
        this.wireLine.setStrokeWidth(WIRE_WIDTH);
        this.wireLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Effect dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 10, 0.2, 0, 0);
        this.wireLine.setEffect(dropShadow);
    }


    private void updateWireLineTransform() {
        // Set control points for cubic curve
        double startX = this.wireLine.getStartX();
        double startY = this.wireLine.getStartY();
        double endX = this.wireLine.getEndX();
        double endY = this.wireLine.getEndY();
        this.wireLine.setControlX1((endX + startX) / 2);
        this.wireLine.setControlY1(startY);
        this.wireLine.setControlX2((endX + startX) / 2);
        this.wireLine.setControlY2(endY);
    }

}
