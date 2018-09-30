package org.cafebabe.viewmodel;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.cafebabe.util.ColorUtil;

public class SelectionBox {

    private Rectangle box;

    /* Public */
    public boolean hasBox() {
        return this.box != null;
    }

    public void createNewBox(double x, double y) {
        this.box = new Rectangle();
        this.box.setStroke(ColorUtil.SELECTION_BOX_STROKE);
        this.box.setFill(ColorUtil.SELECTION_BOX_FILL);
        this.box.setX(x);
        this.box.setY(y);
    }

    public void handleMouseDragged(MouseEvent event) {
        double eventX = event.getX();
        double eventY = event.getY();

        if (!this.hasBox()) {
            createNewBox(eventX, eventY);
            return;
        }

        setNewDimensions(eventX, eventY);
        setNewTransform(eventX, eventY);
    }

    public void handleMouseDragReleased() {
        this.box = null;
    }

    public Node getSelectionBox() {
        return this.box;
    }

    /* Private */
    private void setNewDimensions(double eventX, double eventY) {
        double width = Math.abs(eventX - this.box.getX());
        double height = Math.abs(eventY - this.box.getY());

        this.box.setWidth(width);
        this.box.setHeight(height);
    }

    private void setNewTransform(double eventX, double eventY) {
        this.box.getTransforms().clear();

        double xTransform = (eventX < box.getX()) ? -1.0 : 1.0;
        double yTransform = (eventY < box.getY()) ? -1.0 : 1.0;

        Transform transform = Transform.scale(xTransform, yTransform, this.box.getX(), this.box.getY());
        this.box.getTransforms().add(transform);
    }
}
