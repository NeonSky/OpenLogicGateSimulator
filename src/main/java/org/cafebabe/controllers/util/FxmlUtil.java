package org.cafebabe.controllers.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FxmlUtil {

    /** Attaches the given FXML to the given controller */
    public static void attachFXML(Object controller, String fxmlFilePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass().getResource(fxmlFilePath));
        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void scaleWithAnchorPaneParent(Node child) {
        AnchorPane.setBottomAnchor(child, 0.0);
        AnchorPane.setLeftAnchor(child, 0.0);
        AnchorPane.setRightAnchor(child, 0.0);
        AnchorPane.setTopAnchor(child, 0.0);
    }

}
