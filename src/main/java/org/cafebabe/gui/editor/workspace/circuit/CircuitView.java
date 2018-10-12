package org.cafebabe.gui.editor.workspace.circuit;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.gui.IView;
import org.cafebabe.gui.util.CanvasGridPane;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.model.util.Event;


/**
 * Provides a visual representation of the circuit currently being worked on.
 */
class CircuitView extends AnchorPane implements IView {

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


    @SuppressWarnings("checkstyle:linelength")
    CircuitView() {
        FxmlUtil.attachFxml(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        setupGridPane();
        setupComponentPane();

        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, MouseEvent.MOUSE_PRESSED, this.onHandleMousePress::notifyListeners);
        FxmlUtil.onMySceneLoaded(this, () ->
                FxmlUtil.onSceneInputEvent(getScene(), KeyEvent.KEY_PRESSED, this.onHandleKeyPress::notifyListeners)
        );

        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, DragEvent.DRAG_ENTERED, this.onDragEnter::notifyListeners);
        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, DragEvent.DRAG_EXITED, this.onDragExit::notifyListeners);
        FxmlUtil.onInputEvent(this.componentPane, DragEvent.DRAG_DROPPED, this.onDragDrop::notifyListeners);
        FxmlUtil.onInputEvent(this.componentPane, DragEvent.DRAG_OVER, this.onDragOver::notifyListeners);

        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, MouseEvent.MOUSE_DRAGGED, this.onMouseDrag::notifyListeners);
        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, MouseEvent.MOUSE_RELEASED, this.onMouseDragReleased::notifyListeners);
        FxmlUtil.onInputEventWithMeAsTarget(this.componentPane, MouseEvent.MOUSE_MOVED, this.onMouseMoved::notifyListeners);

        this.componentPane.setOnScroll(this.onScroll::notifyListeners);
    }

    /* Public */
    @Override
    public void destroy() {
        FxmlUtil.destroy(this);
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
