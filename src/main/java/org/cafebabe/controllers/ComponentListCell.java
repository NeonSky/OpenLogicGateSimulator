package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import org.cafebabe.controllers.util.FxmlUtil;

public class ComponentListCell extends AnchorPane {

    @FXML private AnchorPane componentCell;
    @FXML private SVGPath svg;
    @FXML private GridPane grid;
    @FXML private Label componentNameLabel;

    public ComponentListCell(String name, String svgContent) throws RuntimeException {
        FxmlUtil.attachFXML(this, "/view/ComponentListCell.fxml");
        this.bindSizeProperties();
        setComponentNameLabel(name);
        setComponentSvgContent(svgContent);
    }

    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(20));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(20));
    }

    private void setComponentNameLabel(String name) {
        if (name == null) {
            throw new RuntimeException("Can't set a label that is null or empty");
        }

        String upperCasedName = name.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
    }

    private void setComponentSvgContent(String svgContent) {
        if (svgContent == null) {
            throw new RuntimeException("Can't load svgContent that is null or empty");
        }

        this.svg.setContent(svgContent);

        double width = this.svg.prefWidth(-1);
        double height = this.svg.prefHeight(width);
        double scaleX = this.grid.getPrefWidth() / width;
        double scaleY = this.grid.getPrefHeight() / height;

        double finalScaleFactor = Math.min(scaleX, scaleY);

        this.svg.setScaleX(finalScaleFactor);
        this.svg.setScaleY(finalScaleFactor);
    }
}
