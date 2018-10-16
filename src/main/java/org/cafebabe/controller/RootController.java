package org.cafebabe.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import org.cafebabe.controller.editor.EditorController;
import org.cafebabe.model.editor.Editor;
import org.cafebabe.view.editor.EditorView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * The top level controller handler.
 * Switches out the entire content of the scene.
 */
public class RootController implements Initializable {

    @FXML private AnchorPane rootPane;

    /* Public */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadScene(new EditorController(new EditorView(new Editor())));
    }

    /* Private */
    private void loadScene(ISceneController sceneController) {
        this.rootPane.getChildren().clear();
        Node view = sceneController.getView();
        this.rootPane.getChildren().add(view);
        FxmlUtil.scaleWithAnchorPaneParent(view);
    }
}
