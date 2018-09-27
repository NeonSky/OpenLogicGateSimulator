package org.cafebabe.controllers.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.cafebabe.util.ColorUtil;

public class CanvasGridPane extends Pane {

    private final Canvas canvas;

    private static final int GRIDLINE_SPACING = 20;

    public CanvasGridPane() {
        canvas = new Canvas(100, 100);
        getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(ColorUtil.GRID_LINE);
    }

    /* Private */
    private void drawGrid() {
        clearGrid();
        drawHorizontalGridLines();
        drawVerticalGridLines();
    }

    private void clearGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    private void drawHorizontalGridLines() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < getHeight(); i += GRIDLINE_SPACING) {
            gc.strokeLine(0, i, getWidth(), i);
        }
    }

    private void drawVerticalGridLines() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < getWidth(); i += GRIDLINE_SPACING) {
            gc.strokeLine(i, 0, i, getHeight());
        }
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
}