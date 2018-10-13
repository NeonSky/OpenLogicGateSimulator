package org.cafebabe.controller.editor.workspace.circuit;

import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import org.cafebabe.model.editor.util.ComponentUtil;
import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.TrackablePosition;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.editor.componentlist.IComponentProducer;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;


/**
 * Handles dragging and dropping,
 * both when spawning new component (dragging from component list)
 * and when moving existing ones.
 */
public class ComponentDragDropHandler {

    private final ViewModel viewModel;
    private Component dragNewComponent;
    private Position dragStartedPosition;
    private TrackablePosition dragMousePosition;

    public ComponentDragDropHandler(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Public */
    public void onComponentDragDetected(ComponentView componentView, MouseEvent event) {
        Dragboard dragboard = componentView.startDragAndDrop(TransferMode.ANY);
        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent dummyContent = new ClipboardContent();
        dummyContent.put(DataFormat.PLAIN_TEXT, "foo");
        dragboard.setContent(dummyContent);
        dragboard.setDragView(new WritableImage(1, 1));
        Point2D dragStartPoint = this.viewModel.getCamera().getTransform()
                .deltaTransform(event.getX(), event.getY());
        this.dragStartedPosition = new Position(
                (int) dragStartPoint.getX(),
                (int) dragStartPoint.getY()
        );
        this.dragMousePosition = componentView.getComponent().getTrackablePosition();

        event.consume();
    }

    public void onComponentPaneDragEnter(DragEvent event) {
        if (event.getGestureSource() instanceof IComponentProducer) {
            IComponentProducer componentProducer =
                    (IComponentProducer) event.getGestureSource();
            this.dragNewComponent = ComponentUtil.componentFactory(
                    componentProducer.getComponentName()
            );
            this.dragMousePosition = Objects.requireNonNull(this.dragNewComponent)
                    .getTrackablePosition();
            this.viewModel.addComponent(this.dragNewComponent);
            event.consume();
        }
    }

    public void onComponentPaneDragExit(DragEvent event) {
        if (event.getGestureSource() instanceof IComponentProducer
                && this.dragNewComponent != null) {
            this.dragNewComponent.destroy();
            this.dragNewComponent = null;
            event.consume();
        }
    }

    public void onComponentPaneDragDropped(DragEvent event) {
        if (event.getGestureSource() instanceof IComponentProducer) {
            event.setDropCompleted(true);
            event.consume();
            this.dragNewComponent = null;
        }
    }

    public void onComponentPaneDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentView) {
            /* Handle the event if the dragged item is a component view instance */
            handleComponentDragOver(event);
        } else if (event.getGestureSource() instanceof IComponentProducer) {
            /* Accept the event if the dragged item is a ComponentListCellView */
            handleComponentListCellDragOver(event);
        }

        event.consume();
    }

    /* Private */
    private void handleComponentDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        Point2D newModelPosition = this.viewModel.getCamera().getInverseTransform().transform(
                event.getX() - this.dragStartedPosition.getX(),
                event.getY() - this.dragStartedPosition.getY()
        );

        this.dragMousePosition.move(
                (int) newModelPosition.getX(),
                (int) newModelPosition.getY());
    }

    private void handleComponentListCellDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        IComponentProducer componentProducer =
                (IComponentProducer) event.getGestureSource();
        if (!componentProducer.getComponentName().equals(
                this.dragNewComponent.getDisplayName())) {
            throw new UnexpectedComponentDragException("Dragged component from component list is "
                    + "not equal to currently dragged component");
        }
        double height = componentProducer.getHeight();
        double width = componentProducer.getWidth();

        Point2D newModelPosition = this.viewModel.getCamera().getInverseTransform().transform(
                event.getX(),
                event.getY()
        );

        this.dragMousePosition.move(
                (int) (newModelPosition.getX() - width / 2),
                (int) (newModelPosition.getY() - height / 2));
    }
}