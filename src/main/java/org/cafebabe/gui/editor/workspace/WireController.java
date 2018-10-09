package org.cafebabe.gui.editor.workspace;

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
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.model.IReadOnlyMovable;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.ColorUtil;
import org.cafebabe.viewmodel.ISelectable;
import org.cafebabe.viewmodel.ITransformable;


/**
 * Provides a Wire that visually connects two ports in the circuit.
 * It is colored to reflect its current logic state.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class WireController implements ISelectable, ITransformable {

    private static final int WIRE_WIDTH = 6;

    private final CubicCurve wireLine;
    private final Wire wire;
    private boolean isSelected;


    WireController(Wire wire) {
        this.wire = wire;
        this.wire.onWillBeDestroyed().addListener(this::destroy);
        this.wire.onStateChangedEvent().addListener(wire1 -> updateVisualState());
        this.wire.onStartPosMoved.addListener(this::moveStartPointTo);
        this.wire.onEndPosMoved.addListener(this::moveEndPointTo);
        this.wireLine = new CubicCurve();
        this.wireLine.getTransforms().add(0,Transform.scale(1,1));
        setWireDrawingOptions();
        this.wireLine.setPickOnBounds(false);

        setInitialWirePoints();
    }

    /* Public */
    public Wire getWire() {
        return this.wire;
    }

    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.wireLine.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
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

    @Override
    public void disconnectFromWorkspace() {
        this.wire.onStateChangedEvent().removeListener(wire1 -> updateVisualState());
        this.wire.disconnectAll();
    }

    @Override
    public void destroy() {
        FxmlUtil.destroy(this.wireLine);
    }

    @Override
    public IBelongToModel getModelObject() {
        return this.wire;
    }

    @Override
    public void setTransform(Transform transform) {
        this.wireLine.getTransforms().set(0, transform);
    }

    /* Package-Private */
    Node getWireLine() {
        return this.wireLine;
    }

    /* Private */
    private void setWireDrawingOptions() {
        this.wireLine.setStroke(this.getWireColor());
        this.wireLine.setFill(Color.TRANSPARENT);
        this.wireLine.setStrokeWidth(WIRE_WIDTH);
        this.wireLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Effect dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 10, 0.2, 0, 0);
        this.wireLine.setEffect(dropShadow);
    }

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

    private void moveStartPointTo(Position startPoint) {
        this.moveStartPointTo(startPoint.getX(), startPoint.getY());
    }

    private void moveStartPointTo(Number x, Number y) {
        this.wireLine.setStartX(x.doubleValue());
        this.wireLine.setStartY(y.doubleValue());
        updateControlPoints();
    }

    private void moveEndPointTo(Position endPoint) {
        this.moveEndPointTo(endPoint.getX(), endPoint.getY());
    }

    private void moveEndPointTo(Number x, Number y) {
        this.wireLine.setEndX(x.doubleValue());
        this.wireLine.setEndY(y.doubleValue());
        updateControlPoints();
    }

    private void updateControlPoints() {
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

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private void updateVisualState() {
        this.wireLine.setStroke(getWireColor());
        this.wireLine.setVisible(this.wire.isAnyOutputConnected()
                && this.wire.isAnyInputConnected());
    }
}