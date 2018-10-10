package org.cafebabe.gui.editor.workspace.circuit.component.port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.util.EmptyEvent;

/**
 * Port visual.
 */
class PortView extends AnchorPane {

    @FXML Circle connectionNodeCircle;

    final EmptyEvent onClicked = new EmptyEvent();
    private final Port port;

    PortView(Port port, double x, double y) {
        this.port = port;
        FxmlUtil.attachFxml(this, "/view/PortView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.setPickOnBounds(false);
        this.connectionNodeCircle.setCenterX(x);
        this.connectionNodeCircle.setCenterY(y);
        this.connectionNodeCircle.onMouseClickedProperty().setValue(e ->
                this.onClicked.notifyListeners()
        );
    }

    /* Package-Private */
    void updateStyleClasses(boolean isCandidate, String... extraClasses) {
        List<String> styleClasses = new ArrayList<>(Arrays.asList(extraClasses));

        if (this.port.isConnected()) {
            styleClasses.add("connected");
            if (this.port.isHigh()) {
                styleClasses.add("active");
            }
        } else if (isCandidate) {
            styleClasses.add("candidate");
        }

        this.connectionNodeCircle.getStyleClass().setAll(styleClasses);
    }
}
