package org.cafebabe.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.util.ColorUtil;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.Metadata;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.workspace.Position;

import java.util.ArrayList;
import java.util.List;


public class ComponentController extends AnchorPane implements ISelectable {

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private List<PortController> ports = new ArrayList<>();
    private Component component;
    private Position position;
    private boolean isSelected;

    ComponentController(Component component, int x, int y, IWireConnector wireConnector) {
        this(component, new Position(x, y), wireConnector);
    }

    private ComponentController(Component component, Position pos, IWireConnector wireConnector) {
        FxmlUtil.attachFXML(this, "/view/ComponentView.fxml");

        this.addPortsFromMetadata(SvgUtil.getComponentMetadata(component), component, wireConnector);

        svgGroup.getChildren().addAll(this.ports);
        svgGroup.setPickOnBounds(false);

        this.component = component;
        componentSvgPath.setContent(SvgUtil.getBareComponentSvgPath(component));
        componentSvgPath.setStrokeWidth(2);
        componentSvgPath.setFill(ColorUtil.OFFWHITE);

        this.position = pos;
        this.updateVisualState();
    }

    private void addPortsFromMetadata(Metadata componentMetadata, Component component, IWireConnector wireConnector) {
        componentMetadata.inPortMetadata.forEach(m -> ports.add(new InPortController(m.name, m.x, m.y, (InputPort) component.getPort(m.name), wireConnector)));
        componentMetadata.outPortMetadata.forEach(m -> ports.add(new OutPortController(m.name, m.x, m.y, (OutputPort) component.getPort(m.name), wireConnector)));
    }

    public Component getComponent() {
        return this.component;
    }

    Position getPosition() {
        return this.position;
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private void updateVisualState() {
        Color newColor = (this.isSelected) ? ColorUtil.SELECTED : Color.BLACK;
        componentSvgPath.setStroke(newColor);
    }

    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.componentSvgPath.addEventFilter(MouseEvent.MOUSE_CLICKED, listener);
    }

    public void removeClickListener(EventHandler<MouseEvent> listener) {
        this.componentSvgPath.removeEventFilter(MouseEvent.MOUSE_CLICKED, listener);
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
        this.position = null;
        this.svgGroup = null;
        this.componentSvgPath = null;
    }

    @Override
    public IBelongToModel getModelObject() {
        return this.component;
    }
}
