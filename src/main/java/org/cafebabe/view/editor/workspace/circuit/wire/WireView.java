package org.cafebabe.view.editor.workspace.circuit.wire;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Transform;
import lombok.Getter;
import lombok.Setter;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.editor.workspace.camera.IHaveTransform;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.view.View;
import org.cafebabe.view.util.ColorUtil;


/**
 * Provides a Wire that visually connects two ports in the circuit.
 * It is colored to reflect its current logic state.
 */
public class WireView extends View implements IHaveTransform, ISelectable {

    private static final int WIRE_WIDTH = 6;
    @Getter private final CubicCurve wireLine;
    @Getter private final Wire wire;
    @Setter private boolean isSelected;


    public WireView(Wire wire) {
        this.wire = wire;
        this.wireLine = new CubicCurve();
        this.wireLine.getTransforms().add(0,Transform.scale(1,1));
        getChildren().add(this.wireLine);

        this.wire.getOnDestroy().addListener(this::destroy);
        this.wire.getOnStateChanged().addListener((w) -> updateVisualState());
        this.wire.getOnStartPosMoved().addListener(this::moveStartPointTo);
        this.wire.getOnEndPosMoved().addListener(this::moveEndPointTo);

        setInitialWirePoints();
        stylizeWireLine();
        updateVisualState();
    }

    /* Public */
    @Override
    public void setTransform(Transform transform) {
        this.wireLine.getTransforms().set(0, transform);
    }

    @Override
    public void select() {
        setSelected(true);
        updateVisualState();
    }

    @Override
    public void deselect() {
        setSelected(false);
        updateVisualState();
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

    private void moveStartPointTo(Position startPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
    }

    private void moveStartPointTo(Number x, Number y) {
        this.wireLine.setStartX(x.doubleValue());
        this.wireLine.setStartY(y.doubleValue());
        updateWireLineTransform();
    }

    private void moveEndPointTo(Position endPoint) {
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    private void moveEndPointTo(Number x, Number y) {
        this.wireLine.setEndX(x.doubleValue());
        this.wireLine.setEndY(y.doubleValue());
        updateWireLineTransform();
    }

    private void updateVisualState() {
        this.wireLine.setStroke(getWireColor());
        this.wireLine.setVisible(this.wire.isAnyOutputConnected()
                && this.wire.isAnyInputConnected());
    }
    private Color getWireColor() {
        LogicState currentState = this.wire.getLogicState();
        return this.isSelected ? ColorUtil.SELECTED : ColorUtil.getStateColor(currentState);
    }

}
