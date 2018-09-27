package org.cafebabe.viewmodel;

import java.util.HashSet;
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
        if (this.selectionBox == null) {
            double y = event.getY();
            double x = event.getX();
            createNewSelectionRectangle(x, y);
            return;
        }

        double width = Math.abs(event.getX() - this.selectionBox.getX());
        double height = Math.abs(event.getY() - this.selectionBox.getY());

        this.selectionBox.setWidth(width);
        this.selectionBox.setHeight(height);

        this.selectionBox.getTransforms().clear();

        double xMirror = (event.getX() < selectionBox.getX()) ? -1 : 1;
        double yMirror = (event.getY() < selectionBox.getY()) ? -1 : 1;

        Transform t = Transform.scale(xMirror, yMirror, selectionBox.getX(), selectionBox.getY());
        selectionBox.getTransforms().add(t);
    }

    public void handleMouseDragReleased(MouseEvent event) {
        this.selectionBox = null;
    }

    public Rectangle getSelectionBox() {
        return this.selectionBox;
    }

    /* Private */
    private void createNewSelectionRectangle(double x, double y) {
        System.out.println("Created new rectangle.");
        this.selectionBox = new Rectangle();
        this.selectionBox.setStroke(ColorUtil.SELECTION_BOX_STROKE);
        this.selectionBox.setFill(ColorUtil.SELECTION_BOX_FILL);
        this.selectionBox.setX(x);
        this.selectionBox.setY(y);
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

    private void deselect(ISelectable component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }

    private void select(ISelectable component) {
        component.select();
        this.selectedComponents.add(component);
    }
}
