package org.cafebabe.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import org.cafebabe.gui.editor.EditorController;
import org.cafebabe.gui.util.FxmlUtil;

/**
 * The top level controller handler.
 * Switches out the entire content of the screen.
 */
public class RootController implements Initializable {

    @FXML private AnchorPane rootPane;

    /* Public */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadController(new EditorController());
    }

    /* Private */
    private void loadController(IScreenController screenController) {
        this.rootPane.getChildren().clear();
        Node view = screenController.getView();
        this.rootPane.getChildren().add(view);
        FxmlUtil.scaleWithAnchorPaneParent(view);
    }
}
