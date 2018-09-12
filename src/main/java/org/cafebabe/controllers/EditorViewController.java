package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import org.cafebabe.model.components.MockComponent;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;

    private ComponentController mockComponent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mockComponent = new ComponentController(new MockComponent());
        ROOT_PANE.getChildren().add(mockComponent);
    }
}
