package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import org.cafebabe.model.components.Component;

public class ComponentController extends AnchorPane {

    @FXML private SVGPath componentSvgPath;

    private Component component;


    public ComponentController(Component component) {
        attachFXML("/view/ComponentView.fxml");

        this.component = component;
        componentSvgPath.setContent("M40,60 C42,48 44,30 25,32");
    }

    private void attachFXML(String fxmlFilePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
