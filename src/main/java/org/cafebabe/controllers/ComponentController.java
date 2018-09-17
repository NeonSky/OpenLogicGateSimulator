package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.JsonMetadata.ComponentMetadata;
import org.cafebabe.controllers.util.JsonUtil;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Position;
import java.util.ArrayList;
import java.util.List;


class ComponentController extends AnchorPane {

    @FXML private SVGPath componentSvgPath;
    @FXML private Group svgGroup;

    private List<PortController> ports = new ArrayList<>();
    private Component component;
    private Position position;


    private void addPortsFromMetadata(ComponentMetadata componentMetadata) {
        componentMetadata.inPortMetadata.forEach(m->{
            ports.add(new InPortController(m.name, m.x, m.y));
        });

        componentMetadata.outPortMetadata.forEach(m->{
            ports.add(new OutPortController(m.name, m.x, m.y));
        });
    }

    ComponentController(Component component, int x, int y) {
        this(component, new Position(x, y));
    }

    private ComponentController(Component component, Position pos) {
        FxmlUtil.attachFXML(this, "/view/ComponentView.fxml");
        this.addPortsFromMetadata(JsonUtil.getComponentMetadata(component));

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
