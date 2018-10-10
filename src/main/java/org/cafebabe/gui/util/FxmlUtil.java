package org.cafebabe.gui.util;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * A collection of static functions for common, useful FXML operations.
 * Examples: loading FXML, scaling with parent, taking input etc.
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class FxmlUtil {

    private FxmlUtil() {}

    /* Public */
    /**
     * Attaches the given FXML to the given view handler.
     */
    public static void attachFxml(Object viewHandler, String fxmlFilePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(viewHandler.getClass().getResource(fxmlFilePath));
        fxmlLoader.setRoot(viewHandler);
        fxmlLoader.setController(viewHandler);

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

    public static void onMySceneLoaded(Node node, Runnable callback) {
        node.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                callback.run();
            }
        });
    }

    public static <T extends InputEvent> void onSceneInputEvent(
            Scene scene,
            EventType<T> type,
            EventHandler<T> eventHandler) {

        scene.addEventFilter(type, eventHandler);
    }

    public static <T extends InputEvent> void onInputEvent(
            Node controller,
            EventType<T> type,
            EventHandler<T> eventHandler) {

        controller.addEventFilter(type, eventHandler);
    }

    public static <T extends InputEvent> void onInputEventWithMeAsTarget(
            Node controller,
            EventType<T> type,
            EventHandler<T> eventHandler) {

        onInputEvent(controller, type, (e) -> {
            if (e.getTarget() == controller) {
                eventHandler.handle(e);
            }
        });
    }

    public static <T extends InputEvent> void onInputEventWithMeAsSource(
            Node controller,
            EventType<T> type,
            EventHandler<T> eventHandler) {

        onInputEvent(controller, type, (e) -> {
            if (e.getSource() == controller) {
                eventHandler.handle(e);
            }
        });
    }
}
