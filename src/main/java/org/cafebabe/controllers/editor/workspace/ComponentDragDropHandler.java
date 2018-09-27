package org.cafebabe.controllers.editor.workspace;

import java.util.Objects;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import org.cafebabe.controllers.ComponentListCellController;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.util.ComponentUtil;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;
import org.cafebabe.viewmodel.ViewModel;

class ComponentDragDropHandler {

    private final ViewModel viewModel;
    private Component dragNewComponent;
    private Position dragStartedPosition;
    private TrackablePosition dragMousePosition;

    public ComponentDragDropHandler(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* Package-Private */
    void onComponentDragDetected(org.cafebabe.controllers.editor.workspace.component.ComponentController componentController, MouseEvent event) {
        Dragboard dragboard = componentController.startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent dummyContent = new ClipboardContent();
        dummyContent.put(DataFormat.PLAIN_TEXT, "foo");
        dragboard.setContent(dummyContent);
        dragboard.setDragView(new WritableImage(1, 1));
        dragStartedPosition = new Position((int) event.getX(), (int) event.getY());
        dragMousePosition = componentController.getComponent().getTrackablePosition();

        event.consume();
    }

    void onComponentPaneDragEntered(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            ComponentListCellController componentListCellController = (ComponentListCellController) event.getGestureSource();
            dragNewComponent = ComponentUtil.componentFactory(componentListCellController.getComponentName());
            dragMousePosition = Objects.requireNonNull(dragNewComponent).getTrackablePosition();
            viewModel.addComponent(dragNewComponent);
            event.consume();
        }
    }

    void onComponentPaneDragExited(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController && dragNewComponent != null) {
            dragNewComponent.destroy();
            dragNewComponent = null;
            event.consume();
        }
    }

    void onComponentPaneDragDropped(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            event.setDropCompleted(true);
            event.consume();
            dragNewComponent = null;
        }
    }

    void onComponentPaneDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof org.cafebabe.controllers.editor.workspace.component.ComponentController) {
            /* Handle the event if the dragged item is a component controller instance */
            handleComponentDragOver(event);
        } else if (event.getGestureSource() instanceof ComponentListCellController) {
            /* Accept the event if the dragged item is a component list cell controller instance */
            handleComponentListCellDragOver(event);
        }

        event.consume();
    }

    /* Private */
    private void handleComponentDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        dragMousePosition.move(
                (int) event.getX() - dragStartedPosition.getX()
                , (int) event.getY() - dragStartedPosition.getY());
    }

    private void handleComponentListCellDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        ComponentListCellController componentListCellController = (ComponentListCellController) event.getGestureSource();
        if (!componentListCellController.getComponentName().equals(dragNewComponent.getDisplayName())) {
            throw new RuntimeException("Dragged component from component list is not equal to currently dragged component");
        }
        double height = componentListCellController.getHeight();
        double width = componentListCellController.getWidth();
        dragMousePosition.move((int) (event.getX() - (width / 2)), (int) (event.getY() - (height / 2)));
    }
}