package org.cafebabe.view.editor.componentlist;

import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import org.cafebabe.view.View;
import org.cafebabe.view.util.ColorUtil;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Represents a single cell / component, in the component list.
 * Contains a svg image of a component, and can be dragged to spawn that component.
 */
public class ComponentListCellView extends View implements IComponentProducer {

    private final String displayName;

    @FXML private AnchorPane componentCell;
    @FXML private SVGPath svg;
    @FXML private GridPane grid;
    @FXML private Label componentNameLabel;


    public ComponentListCellView(String displayName, String svgContent) {
        this.displayName = displayName;
        FxmlUtil.attachFxml(this, "/view/ComponentListCell.fxml");

        setComponentNameLabel();
        setComponentSvgContent(svgContent);
    }

    /* Public */
    @Override
    public String getComponentName() {
        return this.displayName;
    }


    /* Private */
    private void setComponentNameLabel() {
        if (Strings.isNullOrEmpty(this.displayName)) {
            throw new EmptyLabelException("Can't set a label that is null or empty");
        }

        String upperCasedName = this.displayName.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
        this.componentNameLabel.setWrapText(true);
    }

    private void setComponentSvgContent(String svgContent) {
        if (Strings.isNullOrEmpty(svgContent)) {
            throw new NoSvgContentException("Can't load svgContent that is null or empty");
        }

        this.svg.setContent(svgContent);
        this.scaleComponentSvg();
        this.svg.setStrokeWidth(2);
        this.svg.setStrokeLineCap(StrokeLineCap.SQUARE);
        this.svg.setStroke(ColorUtil.BLACK);
        this.svg.setFill(ColorUtil.TRANSPARENT);
    }

    private void scaleComponentSvg() {

        /*
         * SVG prefWidth and prefHeight methods calculate their results
         * using a content bias. To bypass this, we pass in -1 for width
         * and then use the width to calculate the height.
         */
        double width = this.svg.prefWidth(-1);
        double height = this.svg.prefHeight(width);
        double scaleX = this.grid.getPrefWidth() / width;
        double scaleY = this.grid.getPrefHeight() / height;

        double finalScaleFactor = Math.min(scaleX, scaleY);

        this.svg.setScaleX(finalScaleFactor);
        this.svg.setScaleY(finalScaleFactor);
    }

}
