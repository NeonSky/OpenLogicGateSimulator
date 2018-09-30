package org.cafebabe.controllers.editor.workspace.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import org.cafebabe.controllers.editor.workspace.component.port.InPortController;
import org.cafebabe.controllers.editor.workspace.component.port.OutPortController;
import org.cafebabe.controllers.editor.workspace.component.port.PortController;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.Metadata;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.ColorUtil;
import org.cafebabe.viewmodel.ISelectable;
import org.cafebabe.viewmodel.ViewModel;

public class ComponentController extends AnchorPane implements ISelectable {

    private final List<PortController> ports = new ArrayList<>();
    private final Component component;

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private boolean isSelected;
    private boolean destructionPending;


    public ComponentController(Component component, ViewModel viewModel) {
        FxmlUtil.attachFxml(this, "/view/ComponentView.fxml");

        this.component = component;
        this.addPortsFromMetadata(SvgUtil.getComponentMetadata(component), component, viewModel);

        component.getOnDestroy().addListener(this::destroy);
        component.getTrackablePosition().addPositionListener(this::updatePosition);

        setupFxml();

        this.updateVisualState();
    }

    /* Public */
    public Component getComponent() {
        return this.component;
    }

    @SuppressFBWarnings(value = "UWF_NULL_FIELD",
            justification = "SpotBugs believes @FXML fields are always null")
    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.componentSvgPath.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
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
        this.component.destroy();
        this.ports.clear();
        this.svgGroup = null;
        this.componentSvgPath = null;
    }

    @Override
    public void destroy() {
        if (this.destructionPending) {
            return;
        }
        this.destructionPending = true;
        FxmlUtil.destroy(this);
    }

    @Override
    public IBelongToModel getModelObject() {
        return this.component;
    }

    /* Private */
    @SuppressFBWarnings(value = "UWF_NULL_FIELD",
            justification = "SpotBugs believes @FXML fields are always null")
    private void setupFxml() {
        this.svgGroup.getChildren().addAll(this.ports);
        this.svgGroup.setPickOnBounds(false);

        this.componentSvgPath.setContent(SvgUtil.getBareComponentSvgPath(this.component));
        this.componentSvgPath.setStrokeLineCap(StrokeLineCap.SQUARE);
        this.componentSvgPath.setStrokeWidth(3);
        this.componentSvgPath.setFill(ColorUtil.OFFWHITE);
    }

    private void addPortsFromMetadata(Metadata componentMetadata,
                                      Component component, ViewModel viewModel) {
        componentMetadata.inPortMetadata.forEach(m ->
                this.ports.add(new InPortController(m.x, m.y,
                        (InputPort) component.getPort(m.name), viewModel))
        );
        componentMetadata.outPortMetadata.forEach(m ->
                this.ports.add(new OutPortController(m.x, m.y,
                        (OutputPort) component.getPort(m.name), viewModel))
        );
    }

    private void updateVisualState() {
        Color newColor = this.isSelected ? ColorUtil.SELECTED : Color.BLACK;
        this.componentSvgPath.setStroke(newColor);
    }

    private void updatePosition(Position position) {
        this.setLayoutX(position.getX());
        this.setLayoutY(position.getY());
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
