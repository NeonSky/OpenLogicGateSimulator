package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;

public class ComponentController extends AnchorPane {

    @FXML private SVGPath componentSvgPath;

    private Component component;


    public ComponentController(Component component) {
        FxmlUtil.attachFXML(this, "/view/ComponentView.fxml");

        this.component = component;
        componentSvgPath.setContent(SvgUtil.getComponentSvgPath(component));
    }
}
