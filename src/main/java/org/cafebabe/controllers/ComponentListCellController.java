package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import org.cafebabe.controllers.util.FxmlUtil;

class ComponentListCellController extends AnchorPane {

    final static private int CELL_MARGIN = 20;

    private String displayName;

    @FXML private AnchorPane componentCell;
    @FXML private SVGPath svg;
    @FXML private GridPane grid;
    @FXML private Label componentNameLabel;

    public ComponentListCellController(String name, String svgContent) throws RuntimeException {
        FxmlUtil.attachFXML(this, "/view/ComponentListCell.fxml");
        this.bindSizeProperties();
        this.displayName = name;

        setComponentNameLabel();
        setComponentSvgContent(svgContent);
    }

    @FXML
    private void onDragDetected(MouseEvent event) {
        Dragboard db = startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent c1 = new ClipboardContent();
        c1.put(DataFormat.PLAIN_TEXT, "foo");
        db.setContent(c1);

        event.consume();
    }

    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(CELL_MARGIN));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(CELL_MARGIN));
    }

    private void setComponentNameLabel() {
        if (displayName == null || displayName.isEmpty()) {
            throw new RuntimeException("Can't set a label that is null or empty");
        }

        String upperCasedName = displayName.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
    }

    private void setComponentSvgContent(String svgContent) {
        if (svgContent == null || svgContent.isEmpty()) {
            throw new RuntimeException("Can't load svgContent that is null or empty");
        }

        this.svg.setContent(svgContent);
        this.scaleComponentSVG();
    }

    private void scaleComponentSVG() {

        /*
        * SVG prefWidth and prefHeight methods calculate their results using a content bias.
        * To bypass this, we pass in -1 for width and then use the width to calculate the height.
        */
        double width = this.svg.prefWidth(-1);
        double height = this.svg.prefHeight(width);
        double scaleX = this.grid.getPrefWidth() / width;
        double scaleY = this.grid.getPrefHeight() / height;

        double finalScaleFactor = Math.min(scaleX, scaleY);

        this.svg.setScaleX(finalScaleFactor);
        this.svg.setScaleY(finalScaleFactor);
    }

    String getComponentName() {
        return this.displayName;
    }
}
