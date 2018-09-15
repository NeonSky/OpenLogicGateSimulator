package org.cafebabe.controllers.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasGridPane extends Pane {

    private final Canvas canvas;

    private final int GRIDLINE_SPACING = 20;
    private final Color GRIDLINE_COLOR = Color.LIGHTGRAY;

    public CanvasGridPane() {
        canvas = new Canvas(100, 100);
        getChildren().add(canvas);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);

        drawGrid();
    }

    private void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setStroke(GRIDLINE_COLOR);

        //horizontal lines
        for (int i=0; i < getHeight(); i+=GRIDLINE_SPACING) {
            gc.strokeLine(0, i, getWidth(), i);
        }

        //vertical lines
        for (int i=0; i < getWidth(); i+=GRIDLINE_SPACING) {
            gc.strokeLine(i, 0, i, getHeight());
        }
    }
}