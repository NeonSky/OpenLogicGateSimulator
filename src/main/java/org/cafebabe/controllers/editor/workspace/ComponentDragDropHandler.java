package org.cafebabe.controllers.editor.workspace;

import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.transform.NonInvertibleTransformException;
import org.cafebabe.controllers.editor.ComponentListCellController;
import org.cafebabe.controllers.editor.workspace.component.ComponentController;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.util.ComponentUtil;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;
import org.cafebabe.viewmodel.ViewModel;

/*
Contains method implementations for handling dragging and dropping of components.
 */
class ComponentDragDropHandler {

    private final ViewModel viewModel;
    private Component dragNewComponent;
    private Position dragStartedPosition;
    private TrackablePosition dragMousePosition;

    public ComponentDragDropHandler(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Package-Private */
    void onComponentDragDetected(ComponentController componentController, MouseEvent event) {
        Dragboard dragboard = componentController.startDragAndDrop(TransferMode.ANY);
        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent dummyContent = new ClipboardContent();
        dummyContent.put(DataFormat.PLAIN_TEXT, "foo");
        dragboard.setContent(dummyContent);
        dragboard.setDragView(new WritableImage(1, 1));
        Point2D dragStartPoint = null;

        dragStartPoint = this.viewModel.getCamera().getTransform()
                .deltaTransform(event.getX(), event.getY());
        this.dragStartedPosition = new Position(
                (int) dragStartPoint.getX(),
                (int) dragStartPoint.getY()
        );
        this.dragMousePosition = componentController.getComponent().getTrackablePosition();

        event.consume();
    }

    void onComponentPaneDragEntered(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            ComponentListCellController componentListCellController =
                    (ComponentListCellController) event.getGestureSource();
            this.dragNewComponent = ComponentUtil.componentFactory(
                    componentListCellController.getComponentName()
            );
            this.dragMousePosition = Objects.requireNonNull(this.dragNewComponent)
                    .getTrackablePosition();
            this.viewModel.addComponent(this.dragNewComponent);
            event.consume();
        }
    }

    void onComponentPaneDragExited(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController
                && this.dragNewComponent != null) {
            this.dragNewComponent.destroy();
            this.dragNewComponent = null;
            event.consume();
        }
    }

    void onComponentPaneDragDropped(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            event.setDropCompleted(true);
            event.consume();
            this.dragNewComponent = null;
        }
    }

    void onComponentPaneDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentController) {
            /* Handle the event if the dragged item is a component controller instance */
            handleComponentDragOver(event);
        } else if (event.getGestureSource() instanceof ComponentListCellController) {
            /* Accept the event if the dragged item is a ComponentListCellController */
            handleComponentListCellDragOver(event);
        }

        event.consume();
    }

    /* Private */
    private void handleComponentDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        Point2D newModelPosition = Point2D.ZERO;
        try {
            newModelPosition = this.viewModel.getCamera().getTransform().inverseTransform(
                    event.getX() - this.dragStartedPosition.getX(),
                    event.getY() - this.dragStartedPosition.getY()
            );
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }

        this.dragMousePosition.move(
                (int) newModelPosition.getX(),
                (int) newModelPosition.getY());
    }

    private void handleComponentListCellDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        ComponentListCellController componentListCellController =
                (ComponentListCellController) event.getGestureSource();
        if (!componentListCellController.getComponentName().equals(
                this.dragNewComponent.getDisplayName())) {
            throw new RuntimeException("Dragged component from component list is "
                    + "not equal to currently dragged component");
        }
        double height = componentListCellController.getHeight();
        double width = componentListCellController.getWidth();

        Point2D newModelPosition = Point2D.ZERO;
        try {
            newModelPosition = this.viewModel.getCamera().getTransform().inverseTransform(
                    event.getX(),
                    event.getY()
            );
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }

        this.dragMousePosition.move(
                (int) (newModelPosition.getX() - width / 2),
                (int) (newModelPosition.getY() - height / 2));
    }
}