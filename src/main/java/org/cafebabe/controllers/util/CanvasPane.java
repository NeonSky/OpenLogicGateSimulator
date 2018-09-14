package org.cafebabe.controllers.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class CanvasPane extends Pane {

    private final Canvas canvas;

    public CanvasPane() {
        canvas = new Canvas(100, 100);
        getChildren().add(canvas);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        // Java 9 - snapSize is depricated used snapSizeX() and snapSizeY() accordingly
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);
    }
}