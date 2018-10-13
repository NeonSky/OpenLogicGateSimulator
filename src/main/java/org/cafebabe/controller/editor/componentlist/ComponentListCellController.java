package org.cafebabe.controller.editor.componentlist;

import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import org.cafebabe.controller.Controller;
import org.cafebabe.view.editor.componentlist.ComponentListCellView;

/**
 * Handles user interactions with the component list cell view.
 */
public class ComponentListCellController extends Controller {

    private final ComponentListCellView view;

    public ComponentListCellController(ComponentListCellView view) {
        super(view);
        this.view = view;
        this.view.setOnDragDetected(this::onDragDetected);
    }

    private void onDragDetected(MouseEvent event) {
        Dragboard db = this.view.startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent c1 = new ClipboardContent();
        c1.put(DataFormat.PLAIN_TEXT, "foo");
        db.setContent(c1);
        db.setDragView(new WritableImage(1, 1));

        event.consume();
    }

}
