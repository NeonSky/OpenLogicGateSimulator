package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.Metadata;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.Event;

import java.util.ArrayList;
import java.util.List;


class ComponentController extends AnchorPane {

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private List<PortController> ports = new ArrayList<>();
    private Component component;
    private Position position;


    private void addPortsFromMetadata(Metadata componentMetadata, Component component, IWireConnector wireConnector) {
        componentMetadata.inPortMetadata.forEach(m -> ports.add(new InPortController(m.name, m.x, m.y, (InputPort) component.getPort(m.name), wireConnector)));
        componentMetadata.outPortMetadata.forEach(m -> ports.add(new OutPortController(m.name, m.x, m.y, (OutputPort) component.getPort(m.name), wireConnector)));
    }

    ComponentController(Component component, int x, int y, IWireConnector wireConnector) {
        this(component, new Position(x, y), wireConnector);
    }

    private ComponentController(Component component, Position pos, IWireConnector wireConnector) {
        FxmlUtil.attachFXML(this, "/view/ComponentView.fxml");

        this.addPortsFromMetadata(SvgUtil.getComponentMetadata(component), component, wireConnector);

        svgGroup.getChildren().addAll(this.ports);

        this.component = component;
        componentSvgPath.setContent(SvgUtil.getBareComponentSvgPath(component));

        this.position = pos;
    }

    public Component getComponent() {
        return this.component;
    }

    Position getPosition() {
        return this.position;
    }

}
