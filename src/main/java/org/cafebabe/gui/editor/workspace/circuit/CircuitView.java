package org.cafebabe.gui.editor.workspace.circuit;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.gui.util.CanvasGridPane;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.util.Event;


/**
 * Provides a visual representation of the circuit currently being worked on.
 */
class CircuitView extends AnchorPane {

    @FXML private Pane backgroundPane;
    @FXML private Pane componentPane;

    final Event<MouseEvent> onHandleMousePress = new Event<>();
    final Event<KeyEvent> onHandleKeyPress = new Event<>();

    final Event<DragEvent> onDragEnter = new Event<>();
    final Event<DragEvent> onDragExit = new Event<>();
    final Event<DragEvent> onDragDrop = new Event<>();
    final Event<DragEvent> onDragOver = new Event<>();

    final Event<MouseEvent> onMouseDrag = new Event<>();
    final Event<MouseEvent> onMouseDragReleased = new Event<>();
    final Event<MouseEvent> onMouseMoved = new Event<>();
    final Event<ScrollEvent> onScroll = new Event<>();


    CircuitView() {
        FxmlUtil.attachFxml(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        setupGridPane();
        setupComponentPane();

        //FxmlUtil.onScenePress(this.componentPane, this.onHandleMousePress::notifyListeners);
        FxmlUtil.onClick(this.componentPane, this.onHandleMousePress::notifyListeners);
        FxmlUtil.onSceneKeyPress(this, this.onHandleKeyPress::notifyListeners);

        FxmlUtil.onDragEnter(this.componentPane, this.onDragEnter::notifyListeners);
        FxmlUtil.onDragExit(this.componentPane, this.onDragExit::notifyListeners);
        FxmlUtil.onDragDrop(this.componentPane, this.onDragDrop::notifyListeners);
        FxmlUtil.onDragOver(this.componentPane, this.onDragOver::notifyListeners);

        FxmlUtil.onMouseDragged(this.componentPane, this.onMouseDrag::notifyListeners);
        FxmlUtil.onMouseDragReleased(this.componentPane, this.onMouseDragReleased::notifyListeners);
        FxmlUtil.onMouseMoved(this.componentPane, this.onMouseMoved::notifyListeners);
        FxmlUtil.onScroll(this.componentPane, this.onScroll::notifyListeners);
    }

    /* Package-Private */
    void addToComponentPane(Node node) {
        this.componentPane.getChildren().add(node);
    }

    void removeFromComponentPane(Node node) {
        this.componentPane.getChildren().remove(node);
    }


    /* Private */
    private void setupGridPane() {
        CanvasGridPane gridPane = new CanvasGridPane();
        this.getChildren().add(gridPane);
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
    }

    private void setupComponentPane() {
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();
        FxmlUtil.scaleWithAnchorPaneParent(this.componentPane);
    }

}
