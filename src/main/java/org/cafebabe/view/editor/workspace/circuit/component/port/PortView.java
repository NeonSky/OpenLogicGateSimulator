package org.cafebabe.view.editor.workspace.circuit.component.port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.View;
import org.cafebabe.view.util.FxmlUtil;

/**
 * The common logic for port visuals.
 */
public class PortView extends View {

    @FXML private Circle connectionNodeCircle;

    public final ViewModel viewModel;
    private final Port port;

    public PortView(Port port, double x, double y, ViewModel viewModel) {
        this.port = port;
        this.viewModel = viewModel;

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

    public void updateStyleClasses(boolean isCandidate, String... extraClasses) {
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
