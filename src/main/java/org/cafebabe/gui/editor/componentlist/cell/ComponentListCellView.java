package org.cafebabe.gui.editor.componentlist.cell;

import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import org.cafebabe.gui.IView;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.util.ColorUtil;

/**
 * Represents a single cell / component, in the component list.
 * Contains a svg image of a component, and can be dragged to spawn that component.
 */
class ComponentListCellView extends AnchorPane implements IView, IComponentProducer {

    private static final int CELL_MARGIN = 20;
    private final String displayName;

    @FXML private AnchorPane componentCell;
    @FXML private SVGPath svg;
    @FXML private GridPane grid;
    @FXML private Label componentNameLabel;


    ComponentListCellView(String displayName, String svgContent) {
        FxmlUtil.attachFxml(this, "/view/ComponentListCell.fxml");
        this.bindSizeProperties();
        this.displayName = displayName;

        setComponentNameLabel();
        setComponentSvgContent(svgContent);
    }

    /* Public */
    @Override
    public void destroy() {
        FxmlUtil.destroy(this);
    }

    @Override
    public String getComponentName() {
        return this.displayName;
    }


    /* Private */
    private void bindSizeProperties() {
        this.componentCell.prefHeightProperty().bind(this.heightProperty().subtract(CELL_MARGIN));
        this.componentCell.prefWidthProperty().bind(this.widthProperty().subtract(CELL_MARGIN));
    }

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

    @FXML
    private void onDragDetected(MouseEvent event) {
        Dragboard db = startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent c1 = new ClipboardContent();
        c1.put(DataFormat.PLAIN_TEXT, "foo");
        db.setContent(c1);
        db.setDragView(new WritableImage(1, 1));


        event.consume();
    }
}
