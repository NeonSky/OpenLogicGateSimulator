package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import org.cafebabe.model.components.MockComponent;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;
    @FXML private FlowPane componentFlowPane;

    private ComponentController mockComponent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mockComponent = new ComponentController(new MockComponent());
        ROOT_PANE.getChildren().add(mockComponent);
        addDummyComponents();
    }

    private void addDummyComponents() {
        List<ComponentListCell> listCells = new ArrayList<>();
        for(int i = 0; i<100; i++) {
            listCells.add(new ComponentListCell());
        }

        componentFlowPane.getChildren().addAll(listCells);
    }
}
