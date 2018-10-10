package org.cafebabe.gui.util;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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

    public static void onScenePress(Node controller, EventHandler<MouseEvent> handleMousePress) {
        onSceneAvailable(controller, scene ->
                scene.addEventFilter(MouseEvent.MOUSE_PRESSED, handleMousePress)
        );
    }

    public static void onClick(Node controller, EventHandler<MouseEvent> handleMouseClick) {
        controller.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (e.getTarget() == controller) {
                handleMouseClick.handle(e);
            }
        });
    }

    public static void onSceneKeyPress(Node controller, EventHandler<KeyEvent> handleKeyPress) {
        onSceneAvailable(controller, scene -> scene.setOnKeyPressed(handleKeyPress));
    }

    public static void onKeyPress(Node controller, EventHandler<KeyEvent> handleKeyPress) {
        controller.setOnKeyPressed(handleKeyPress);
    }

    public static void onScroll(Node controller, EventHandler<ScrollEvent> handleScroll) {
        controller.setOnScroll(handleScroll);
    }

    public static void onMouseDragged(Node controller, EventHandler<MouseEvent> handleMouseDrag) {
        controller.addEventFilter(MouseEvent.MOUSE_DRAGGED, (e) -> {
            if (e.getTarget() == controller) {
                handleMouseDrag.handle(e);
            }
        });
    }

    public static void onMouseMoved(Node controller, EventHandler<MouseEvent> handleMouseMove) {
        controller.addEventFilter(MouseEvent.MOUSE_MOVED, (e) -> {
            if (e.getTarget() == controller) {
                handleMouseMove.handle(e);
            }
        });
    }

    public static void onMouseDragReleased(Node controller,
                                           EventHandler<MouseEvent> handleMouseDragReleased) {
        controller.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
            if (e.getTarget() == controller) {
                handleMouseDragReleased.handle(e);
            }
        });
    }

    public static void onDragEnter(Node controller,
                                           EventHandler<DragEvent> handleDragEnter) {
        controller.addEventFilter(DragEvent.DRAG_ENTERED, (e) -> {
            if (e.getTarget() == controller) {
                handleDragEnter.handle(e);
            }
        });
    }

    public static void onDragExit(Node controller,
                                   EventHandler<DragEvent> handleDragExit) {
        controller.addEventFilter(DragEvent.DRAG_EXITED, (e) -> {
            if (e.getTarget() == controller) {
                handleDragExit.handle(e);
            }
        });
    }

    public static void onDragDrop(Node controller,
                                  EventHandler<DragEvent> handleDragDrop) {
        controller.addEventHandler(DragEvent.DRAG_DROPPED, handleDragDrop);
    }

    public static void onDragOver(Node controller,
                                  EventHandler<DragEvent> handleDragOver) {
        controller.addEventHandler(DragEvent.DRAG_OVER, handleDragOver);
    }

    /* Private */
    private static void onSceneAvailable(Node node, Consumer<Scene> callback) {
        if (node.getScene() != null) {
            callback.accept(node.getScene());
            return;
        }

        final ChangeListener<Scene> sceneChangeListener =
                (observableScene, oldScene, newScene) -> {
                    if (newScene != null) {
                        callback.accept(newScene);
                    }
                };

        node.sceneProperty().addListener(sceneChangeListener);
    }
}
