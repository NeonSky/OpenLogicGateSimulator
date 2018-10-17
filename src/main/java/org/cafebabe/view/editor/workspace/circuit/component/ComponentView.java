package org.cafebabe.view.editor.workspace.circuit.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import lombok.Getter;
import net.javainthebox.caraibe.svg.SvgContent;
import org.cafebabe.controller.editor.workspace.circuit.selection.ComponentDragDropHandler;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.model.editor.util.SvgUtil;
import org.cafebabe.model.editor.workspace.camera.IHaveTransform;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentData;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.PortView;
import org.cafebabe.view.util.ColorUtil;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Provides a visual representation of a component with its ports.
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports"})
public class ComponentView extends View implements IHaveTransform, ISelectable {

    public final ComponentDragDropHandler componentDragDropHandler;

    @FXML
    private Group componentSvgContainer;
    @FXML
    private Group svgGroup;

    @Getter
    private final Component component;
    @Getter
    private final List<PortView> portViews = new ArrayList<>();
    private boolean isSelected;
    private Transform transform = Transform.scale(1, 1);


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public ComponentView(
            Component component,
            ComponentDragDropHandler componentDragDropHandler) {

        FxmlUtil.attachFxml(this, "/view/ComponentView.fxml");

        this.component = component;
        this.componentDragDropHandler = componentDragDropHandler;

        this.svgGroup.getChildren().addAll(this.portViews);
        this.svgGroup.setPickOnBounds(false);

        SvgContent svg = SvgUtil.loadComponentSvg(component);
        this.componentSvgContainer.getChildren().setAll(svg);
        svg.setPickOnBounds(false);

        svg.selectNodesWithClasses("component-body").forEach(
                ComponentView::setDefaultStyle
        );

        initTransforms();
        updateVisualState();
        updatePosition(this.component.getTrackablePosition());
    }

    /* Public */
    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
        updateTransform();
    }

    public void addPortsFromMetadata(ComponentData componentMetadata,
                                     Component component) {
        componentMetadata.inPortMetadata.forEach(m -> {
            InPortView view = new InPortView(
                    (InputPort) component.getPort(m.name),
                    m.x,
                    m.y);
            this.portViews.add(view);
            addSubview(this.svgGroup, view);
        });

        componentMetadata.outPortMetadata.forEach(m -> {
            OutPortView view = new OutPortView(
                    (OutputPort) component.getPort(m.name),
                    m.x,
                    m.y);
            this.portViews.add(view);
            addSubview(this.svgGroup, view);
        });
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

    public void updatePosition(Position position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.updateTransform();
    }

    public SvgContent getComponentSvg() {
        return (SvgContent) this.componentSvgContainer.getChildren().get(0);
    }

    public boolean isIntersecting(Bounds bounds) {
        Bounds localBounds = this.parentToLocal(bounds);
        Bounds groupBounds = this.svgGroup.parentToLocal(localBounds);
        Bounds containerBounds = this.componentSvgContainer.parentToLocal(groupBounds);
        return getComponentSvg().intersects(containerBounds);
    }

    public void highlightInputPorts() {
        this.portViews.forEach((p) ->
                p.setHighlighted(p.getPort() instanceof InputPort)
        );
    }

    public void highlightOutputPorts() {
        this.portViews.forEach((p) ->
                p.setHighlighted(p.getPort() instanceof OutputPort)
        );
    }

    public void unhighlightPorts() {
        this.portViews.forEach((p) -> p.setHighlighted(false));
    }

    /* Private */

    private static void setDefaultStyle(Node n) {
        Shape shape = (Shape) n;
        shape.setStrokeLineCap(StrokeLineCap.SQUARE);
        shape.setStrokeWidth(3);
        shape.setFill(ColorUtil.OFFWHITE);
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void updateVisualState() {
        Color newColor = this.isSelected ? ColorUtil.SELECTED : Color.BLACK;
        SvgContent svg = getComponentSvg();
        svg.selectNodesWithClasses("component-body").forEach(
                n -> ((Shape) n).setStroke(newColor)
        );

        for (Map.Entry<String, Boolean> tag : this.component.getExtraStateData().entrySet()) {
            svg.selectNodesWithClasses("visible-if-" + (tag.getKey())).forEach(
                    n -> n.setVisible(tag.getValue())
            );
            svg.selectNodesWithClasses("visible-unless-" + tag.getValue()).forEach(
                    n -> n.setVisible(!tag.getValue())
            );
        }
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
            this.getTransforms().add(i, Transform.scale(1, 1));
        }
    }

    private void resetTransforms() {
        for (int i = 0; i < 3; i++) {
            this.getTransforms().set(i, Transform.scale(1, 1));
        }
    }
}
