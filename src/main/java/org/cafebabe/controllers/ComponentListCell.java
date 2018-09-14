package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import org.cafebabe.controllers.util.FxmlUtil;

import java.io.IOException;

public class ComponentListCell extends AnchorPane {

    // Instance variables
    @FXML private AnchorPane componentCell;
    @FXML private SVGPath svg;
    @FXML private Label componentNameLabel;

    // Constructors
    public ComponentListCell(String name, String svgContent) throws RuntimeException {
        FxmlUtil.attachFXML(this, "/view/ComponentListCell.fxml");
        this.bindSizeProperties();
        setComponentNameLabel(name);
        setComponentSvgContent(svgContent);
    }

    /**
     * Binds the size attributes of the component cell and the component image to this size.
     */
    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(20));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(20));
    }

    /**
     * Sets the text attribute of this component cells name label.
     * @param name The name to display
     */
    private void setComponentNameLabel(String name) {
        if (name == null) {
            throw new RuntimeException("Can't set a label that is null or empty");
        }

        String upperCasedName = name.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
    }

    /**
     * Sets the SVG content to be displayed by this cell.
     * @param svgContent The SVG content to be displayed
     */
    private void setComponentSvgContent(String svgContent) {
        if (svgContent == null) {
            throw new RuntimeException("Can't load svgContent that is null or empty");
        }

        this.svg.setContent(svgContent);
    }
}
