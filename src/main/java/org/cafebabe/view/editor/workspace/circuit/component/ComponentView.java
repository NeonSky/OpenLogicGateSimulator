package org.cafebabe.view.editor.workspace.circuit.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import org.cafebabe.controller.editor.workspace.circuit.ComponentDragDropHandler;
import org.cafebabe.model.editor.util.SvgUtil;
import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.Metadata;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.removemeplz.ISelectable;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.PortView;
import org.cafebabe.view.util.ColorUtil;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Provides a visual representation of a component with its ports.
 */
public class ComponentView extends View implements ISelectable {

    public final ViewModel viewModel;
    public final ComponentDragDropHandler componentDragDropHandler;

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private final Component component;
    private final List<PortView> portViews = new ArrayList<>();
    private boolean isSelected;
    private Transform transform = Transform.scale(1,1);


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public ComponentView(
            Component component,
            ViewModel viewModel,
            ComponentDragDropHandler componentDragDropHandler) {

        FxmlUtil.attachFxml(this, "/view/ComponentView.fxml");

        this.component = component;
        this.viewModel = viewModel;
        this.componentDragDropHandler = componentDragDropHandler;

        this.svgGroup.getChildren().addAll(this.portViews);
        this.svgGroup.setPickOnBounds(false);

        this.componentSvgPath.setContent(SvgUtil.getBareComponentSvgPath(component));
        this.componentSvgPath.setStrokeLineCap(StrokeLineCap.SQUARE);
        this.componentSvgPath.setStrokeWidth(3);
        this.componentSvgPath.setFill(ColorUtil.OFFWHITE);

        initTransforms();
        updateVisualState();
    }

    /* Public */
    public void setTransform(Transform transform) {
        this.transform = transform;
        updateTransform();
    }

    public Component getComponent() {
        return this.component;
    }

    public void addPortsFromMetadata(Metadata componentMetadata,
                                     Component component, ViewModel viewModel) {
        componentMetadata.inPortMetadata.forEach(m -> {
            InPortView view = new InPortView(
                            (InputPort) component.getPort(m.name),
                            m.x,
                            m.y,
                            viewModel);
            this.portViews.add(view);
            addSubview(this.svgGroup, view);
        });

        componentMetadata.outPortMetadata.forEach(m -> {
            OutPortView view = new OutPortView(
                    (OutputPort) component.getPort(m.name),
                    m.x,
                    m.y,
                    viewModel);
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

    public SVGPath getComponentSvgPath() {
        return this.componentSvgPath;
    }


    /* Private */
    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private void updateVisualState() {
        Color newColor = this.isSelected ? ColorUtil.SELECTED : Color.BLACK;
        this.componentSvgPath.setStroke(newColor);
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
