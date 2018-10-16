package org.cafebabe.view.editor.componentlist;

import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;
import net.javainthebox.caraibe.svg.SvgContent;
import org.cafebabe.view.View;
import org.cafebabe.view.util.ColorUtil;
import org.cafebabe.view.util.FxmlUtil;
/**
 * Represents a single cell / component, in the component list.
 * Contains a svg image of a component, and can be dragged to spawn that component.
 */
public class ComponentListCellView extends View implements IComponentProducer {

    private final String displayName;
    private final String identifier;

    @FXML private AnchorPane componentCell;
    @FXML private Group svgContainer;
    @FXML private GridPane grid;
    @FXML private Label componentNameLabel;


    public ComponentListCellView(String displayName,
                                 String identifier,
                                 Group svg,
                                 String description) {

        this.displayName = displayName;
        this.identifier = identifier;
        FxmlUtil.attachFxml(this, "/view/ComponentListCell.fxml");

        setComponentNameLabel();
        setComponentSvg(svg);
        setComponentDescription(description);
    }

    private static void setComponentStyle(Node n) {
        Shape shape = (Shape) n;
        shape.setStrokeLineCap(StrokeLineCap.SQUARE);
        shape.setStrokeWidth(3);
        shape.setFill(ColorUtil.OFFWHITE);
    }

    /* Public */
    @Override
    public String getComponentIdentifier() {
        return this.identifier;
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

    private void setComponentSvg(Group svg) {
        ((SvgContent) svg)
                .selectNodes("component-body").forEachRemaining(
                ComponentListCellView::setComponentStyle
        );
        ((SvgContent) svg)
                .selectNodes("wire-preview").forEachRemaining(
                ComponentListCellView::setWirePreviewStyle
        );
        this.svgContainer.getChildren().setAll(svg);
        scaleComponentSvg();
    }

    private void setComponentDescription(String description) {
        Tooltip tooltip = new Tooltip(description);
        Tooltip.install(this.componentCell, tooltip);
        tooltip.setShowDelay(new Duration(500));
        tooltip.setHideDelay(new Duration(0));
    }

    private static void setWirePreviewStyle(Node node) {
        Shape shape = (Shape) node;
        shape.setStrokeWidth(2);
        shape.setStroke(Paint.valueOf("black"));
    }

    private void scaleComponentSvg() {

        /*
         * SVG prefWidth and prefHeight methods calculate their results
         * using a content bias. To bypass this, we pass in -1 for width
         * and then use the width to calculate the height.
         */
        double width = this.svgContainer.prefWidth(-1);
        double height = this.svgContainer.prefHeight(width);
        double scaleX = this.grid.getPrefWidth() / width;
        double scaleY = this.grid.getPrefHeight() / height;

        double finalScaleFactor = Math.min(scaleX, scaleY);

        this.svgContainer.setScaleX(finalScaleFactor);
        this.svgContainer.setScaleY(finalScaleFactor);
    }

}
