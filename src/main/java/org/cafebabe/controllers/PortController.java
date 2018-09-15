package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.cafebabe.controllers.util.FxmlUtil;

public abstract class PortController extends AnchorPane {

    @FXML
    protected Circle connectionNodeCircle;
    private final String name;

    public PortController(String name, double x, double y) {
        FxmlUtil.attachFXML(this, "/view/PortView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.name = name;

        connectionNodeCircle.setCenterX(x);
        connectionNodeCircle.setCenterY(y);
    }
}
