package org.cafebabe.view.editor.workspace.circuit.component.port;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.view.View;
import org.cafebabe.view.util.FxmlUtil;

/**
 * The common logic for port visuals.
 */
public class PortView extends View {

    @Getter @FXML private Circle connectionNodeCircle;
    @Getter @Setter private List<String> persistentStyles;

    private final Port port;
    private boolean isHighlighted;

    public PortView(Port port, double x, double y) {
        this.port = port;

        FxmlUtil.attachFxml(this, "/view/PortView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.setPickOnBounds(false);
        this.connectionNodeCircle.setCenterX(x);
        this.connectionNodeCircle.setCenterY(y);
    }

    /* Public */
    public Port getPort() {
        return this.port;
    }

    public Circle getConnectionNodeCircle() {
        return this.connectionNodeCircle;
    }

    public void updateStyleClasses() {
        List<String> styleClasses = new ArrayList<>(getPersistentStyles());

        if (this.port.isConnected()) {
            styleClasses.add("connected");
            if (this.port.isHigh()) {
                styleClasses.add("active");
            }
        } else if (this.isHighlighted) {
            styleClasses.add("candidate");
        }

        this.connectionNodeCircle.getStyleClass().setAll(styleClasses);
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
        updateStyleClasses();
    }

}
