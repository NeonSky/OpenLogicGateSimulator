package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.cafebabe.util.ColorUtil;

class ControllerSelector {

    private final Set<ISelectable> selectedComponents = new HashSet<>();
    private Rectangle selectionBox;

    /* Public */
    public void deleteSelectedControllers() {
        for (ISelectable comp : this.selectedComponents) {
            comp.getModelObject().destroy();
        }
        this.selectedComponents.clear();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleClick(component);
        }
    }

    public void clearSelection() {
        this.selectedComponents.forEach(ISelectable::deselect);
        this.selectedComponents.clear();
    }

    public void handleMouseDragged(MouseEvent event) {
        double eventX = event.getX();
        double eventY = event.getY();

        if (this.selectionBox == null) {
            createNewSelectionRectangle(eventX, eventY);
            return;
        }

        setSelectionBoxDimensions(eventX, eventY);
        setSelectionBoxTransform(eventX, eventY);
    }

    public void handleMouseDragReleased(MouseEvent event) {
        this.selectionBox = null;
    }

    public void select(List<ISelectable> selectables) {
        selectables.forEach(comp -> {
            selectedComponents.add(comp);
            comp.select();
        });
    }

    public void select(ISelectable component) {
        component.select();
        this.selectedComponents.add(component);
    }

    public void deselect(List<ISelectable> selectables) {
        selectables.forEach(comp -> {
            selectedComponents.remove(comp);
            comp.deselect();
        });
    }

    public void deselect(ISelectable component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }

    public Rectangle getSelectionBox() {
        return this.selectionBox;
    }

    /* Private */
    private void createNewSelectionRectangle(double x, double y) {
        this.selectionBox = new Rectangle();
        this.selectionBox.setStroke(ColorUtil.SELECTION_BOX_STROKE);
        this.selectionBox.setFill(ColorUtil.SELECTION_BOX_FILL);
        this.selectionBox.setX(x);
        this.selectionBox.setY(y);
    }

    private void setSelectionBoxDimensions(double x, double y) {
        double width = Math.abs(x - this.selectionBox.getX());
        double height = Math.abs(y - this.selectionBox.getY());

        this.selectionBox.setWidth(width);
        this.selectionBox.setHeight(height);
    }

    private void setSelectionBoxTransform(double eventX, double eventY) {
        this.selectionBox.getTransforms().clear();

        double xTransform = (eventX < selectionBox.getX()) ? -1 : 1;
        double yTransform = (eventY < selectionBox.getY()) ? -1 : 1;

        Transform t = Transform.scale(xTransform, yTransform, selectionBox.getX(), selectionBox.getY());
        this.selectionBox.getTransforms().add(t);
    }

    private void handleShiftClick(ISelectable component) {
        if (this.selectedComponents.contains(component)) {
            deselect(component);
        } else {
            select(component);
        }
    }

    private void handleClick(ISelectable component) {
        this.clearSelection();
        this.select(component);
    }
}
