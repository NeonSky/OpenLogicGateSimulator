package org.cafebabe.gui.editor.workspace.circuit.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import org.cafebabe.gui.editor.workspace.circuit.component.port.PortController;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.gui.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.ColorUtil;

/**
 * Component visual.
 */
class ComponentView extends AnchorPane implements IComponentView {

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private boolean isSelected;
    private Transform transform = Transform.scale(1,1);


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    ComponentView(Component component, List<PortController> ports) {
        FxmlUtil.attachFxml(this, "/view/ComponentView.fxml");

        component.getTrackablePosition().addPositionListener(this::updatePosition);

        List<Node> portViews = new ArrayList<>();
        ports.forEach(p -> portViews.add(p.getView()));
        this.svgGroup.getChildren().addAll(portViews);
        this.svgGroup.setPickOnBounds(false);

        this.componentSvgPath.setContent(SvgUtil.getBareComponentSvgPath(component));
        this.componentSvgPath.setStrokeLineCap(StrokeLineCap.SQUARE);
        this.componentSvgPath.setStrokeWidth(3);
        this.componentSvgPath.setFill(ColorUtil.OFFWHITE);

        initTransforms();
        updateVisualState();
    }

    /* Package-Private */
    @SuppressFBWarnings(value = "UWF_NULL_FIELD",
            justification = "SpotBugs believes @FXML fields are always null")
    void addClickListener(EventHandler<MouseEvent> listener) {
        this.componentSvgPath.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
    }

    void updateVisualState() {
        Color newColor = this.isSelected ? ColorUtil.SELECTED : Color.BLACK;
        this.componentSvgPath.setStroke(newColor);
    }

    void setTransform(Transform transform) {
        this.transform = transform;
        updateTransform();
    }

    void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    void destroy() {
        this.svgGroup = null;
        this.componentSvgPath = null;
    }


    /* Private */
    private void updatePosition(Position position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.updateTransform();
    }

    private void updateTransform() {
        resetTransforms();
        transformInBase(this.transform, this.getLocalToParentTransform());
    }

    private void transformInBase(Transform transform, Transform base) {
        try {
            this.getTransforms().set(0, base.createInverse());
            this.getTransforms().set(1, transform);
            this.getTransforms().set(2, base);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    private void initTransforms() {
        for (int i = 0; i < 3; i++) {
            this.getTransforms().add(i,Transform.scale(1,1));
        }
    }

    private void resetTransforms() {
        for (int i = 0; i < 3; i++) {
            this.getTransforms().set(i,Transform.scale(1,1));
        }
    }

}
