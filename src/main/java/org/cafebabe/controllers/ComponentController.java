package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.workspace.Position;

public class ComponentController extends AnchorPane {

    @FXML private SVGPath componentSvgPath;

    private Component component;
    private Position position;

    public ComponentController(Component component, int x, int y) {
        this(component, new Position(x, y));
    }

    public ComponentController(Component component, Position pos) {
        FxmlUtil.attachFXML(this, "/view/ComponentView.fxml");

        this.component = component;
        componentSvgPath.setContent(SvgUtil.getComponentSvgPath(component));

        this.position = pos;
    }

    public Component getComponent() {
        return this.component;
    }

}
