package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.FlowPane;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.MockComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;
    @FXML private FlowPane componentFlowPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDummyComponents();
    }

    private void addDummyComponents() {
        MockComponent mock = new MockComponent();
        List<ComponentListCell> listCells = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            listCells.add(new ComponentListCell(mock.getDisplayName(), SvgUtil.getComponentSvgPath(mock)));
        }

        componentFlowPane.getChildren().addAll(listCells);
    }
}
