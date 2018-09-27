package org.cafebabe.controllers.util;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class FxmlUtil {

    /* Public */

    /**
     * Attaches the given FXML to the given controller
     */
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

    public static void destroy(Node node) {
        Parent parent = node.getParent();
        if (parent != null) {
            if (parent instanceof Pane) {
                ((Pane) parent).getChildren().remove(node);
            } else {
                parent.getChildrenUnmodifiable().remove(node);
            }
        }
    }

    public static void onSceneClick(Node controller, EventHandler<MouseEvent> handleMouseClick) {
        onSceneAvailible(controller, scene -> scene.addEventFilter(MouseEvent.MOUSE_CLICKED, handleMouseClick));
    }

    public static void onClick(Node controller, EventHandler<MouseEvent> handleMouseClick) {
        controller.addEventFilter(MouseEvent.MOUSE_CLICKED, handleMouseClick);
    }

    public static void onSceneKeyPress(Node controller, EventHandler<KeyEvent> handleKeyPress) {
        onSceneAvailible(controller, scene -> scene.setOnKeyPressed(handleKeyPress));
    }

    public static void onKeyPress(Node controller, EventHandler<KeyEvent> handleKeyPress) {
        controller.setOnKeyPressed(handleKeyPress);
    }

    /* Private */
    private static void onSceneAvailible(Node node, Consumer<Scene> callback) {
        if (node.getScene() != null) {
            callback.accept(node.getScene());
            return;
        }

        final ChangeListener<Scene> SCENE_CHANGE_LISTENER = (observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                callback.accept(newScene);
            }
        };

        node.sceneProperty().addListener(SCENE_CHANGE_LISTENER);
    }
}
