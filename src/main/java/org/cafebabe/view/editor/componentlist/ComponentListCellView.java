package org.cafebabe.view.editor.componentlist;

import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.Set;
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
import lombok.Getter;
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
    @Getter private final String identifier;

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

    /* Private */
    private void setComponentNameLabel() {
        if (Strings.isNullOrEmpty(this.displayName)) {
            throw new EmptyLabelException("Can't set a label that is null or empty");
        }

        String upperCasedName = this.displayName.toUpperCase();
        this.componentNameLabel.setText(upperCasedName);
        this.componentNameLabel.setWrapText(true);
    }

    private void setComponentSvg(Group svgNode) {
        SvgContent svg = (SvgContent) svgNode;
        svg.selectNodesWithClasses("component-body", "wire-preview", "hide-in-preview")
                .forEach(this::setSvgNodeStyle);

        this.svgContainer.getChildren().setAll(svg);
        resizeComponentSvg();
    }

    private void setSvgNodeStyle(Node node) {
        Set<String> nodeCssClasses = new HashSet<>(node.getStyleClass());
        Shape shape = (Shape) node;
        if (nodeCssClasses.contains("component-body")) {
            shape.setStrokeLineCap(StrokeLineCap.SQUARE);
            shape.setStrokeWidth(3);
            shape.setFill(ColorUtil.OFFWHITE);
        } else if (nodeCssClasses.contains("wire-preview")) {
            shape.setStrokeWidth(2);
            shape.setStroke(Paint.valueOf("black"));
        } else if (nodeCssClasses.contains("hide-in-preview")) {
            node.setVisible(false);
        }

    }

    private void setComponentDescription(String description) {
        Tooltip tooltip = new Tooltip(description);
        Tooltip.install(this.componentCell, tooltip);
        tooltip.setShowDelay(new Duration(500));
        tooltip.setHideDelay(new Duration(0));
        tooltip.setShowDuration(new Duration(5000));
    }

    private void resizeComponentSvg() {

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
