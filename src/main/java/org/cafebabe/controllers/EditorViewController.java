package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.FlowPane;
import org.cafebabe.model.components.MockComponent;

import java.util.ResourceBundle;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;
    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;

    private ComponentController mockComponent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebarAnchorPane.getChildren().add(new ComponentListController());
        mockComponent = new ComponentController(new MockComponent(), 0, 0);
        workspacesPane.getChildren().add(mockComponent);
    }
}
