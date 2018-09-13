package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controllers.util.FxmlUtil;

import java.io.IOException;

public class ComponentListCell extends AnchorPane {

    // Instance variables
    @FXML private AnchorPane componentCell;
    @FXML private ImageView componentImage;
    @FXML private Label componentNameLabel;

    // Constructors
    public ComponentListCell() throws RuntimeException {
        FxmlUtil.attachFXML(this, "/view/ComponentListCell.fxml");
        this.bindSizeProperties();
    }

    /**
     * Binds the size attributes of the component cell and the component image to this size.
     */
    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(20));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(20));
        this.componentImage.fitHeightProperty().bind(this.componentCell.widthProperty().subtract(30));
        this.componentImage.fitWidthProperty().bind(this.componentCell.widthProperty().subtract(30));
    }

    /**
     * Sets the text attribute of this component cells name label.
     * @param name The name to display
     */
    private void setComponentNameLabel(String name) {
        if (name.isEmpty() || name == null) { return; }

        String upperCasedName = name.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
    }

    /**
     * Sets the image to be displayed by this cell.
     * @param image The image to be displayed
     */
    private void setComponentImage(Image image) {
        if (image == null) { return; }

        this.componentImage.setImage(image);
    }
}
