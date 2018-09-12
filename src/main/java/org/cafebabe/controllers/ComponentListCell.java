package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ComponentListCell extends AnchorPane {

    // Instance variables
    @FXML AnchorPane componentCell;
    @FXML ImageView componentImage;
    @FXML Label componentNameLabel;

    // Constructors
    public ComponentListCell() throws RuntimeException {
        this.loadFXML();
        this.bindSizeProperties();
    }

    // Instance methods
    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ComponentListCell.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(20));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(20));
        this.componentImage.fitHeightProperty().bind(this.componentCell.widthProperty().subtract(30));
        this.componentImage.fitWidthProperty().bind(this.componentCell.widthProperty().subtract(30));
    }
}
