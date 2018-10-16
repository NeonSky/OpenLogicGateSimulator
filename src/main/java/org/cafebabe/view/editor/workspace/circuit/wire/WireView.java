package org.cafebabe.view.editor.workspace.circuit.wire;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Transform;
import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.View;
import org.cafebabe.view.util.ColorUtil;


/**
 * Provides a Wire that visually connects two ports in the circuit.
 * It is colored to reflect its current logic state.
 */
public class WireView extends View {

    public final ViewModel viewModel;

    private static final int WIRE_WIDTH = 6;
    private final CubicCurve wireLine;
    private final Wire wire;
    private boolean isSelected;


    public WireView(Wire wire, ViewModel viewModel) {
        this.wire = wire;
        this.viewModel = viewModel;
        this.wireLine = new CubicCurve();
        this.wireLine.getTransforms().add(0,Transform.scale(1,1));

        this.setPickOnBounds(false);
        this.wireLine.setPickOnBounds(false);
        getChildren().add(this.wireLine);

        setInitialWirePoints();
        stylizeWireLine();
    }

    /* Public */
    public void moveStartPointTo(Position startPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
    }

    public void moveStartPointTo(Number x, Number y) {
        this.wireLine.setStartX(x.doubleValue());
        this.wireLine.setStartY(y.doubleValue());
        updateWireLineTransform();
    }

    public void moveEndPointTo(Position endPoint) {
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    public void moveEndPointTo(Number x, Number y) {
        this.wireLine.setEndX(x.doubleValue());
        this.wireLine.setEndY(y.doubleValue());
        updateWireLineTransform();
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void updateVisualState() {
        this.wireLine.setStroke(getWireColor());
        this.wireLine.setVisible(this.wire.isAnyOutputConnected()
                && this.wire.isAnyInputConnected());
    }

    public void setTransform(Transform transform) {
        this.wireLine.getTransforms().set(0, transform);
    }

    public CubicCurve getWireLine() {
        return this.wireLine;
    }

    public Wire getWire() {
        return this.wire;
    }


    /* Private */
    private Color getWireColor() {
        LogicState currentState = this.wire.logicState();
        return this.isSelected ? ColorUtil.SELECTED : ColorUtil.getStateColor(currentState);
    }

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
