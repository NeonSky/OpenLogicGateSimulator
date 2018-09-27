package org.cafebabe.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.viewmodel.ViewModel;

public abstract class PortController extends AnchorPane {

    final ViewModel viewModel;
    @FXML
    Circle connectionNodeCircle;

    PortController(double x, double y, ViewModel viewModel) {
        FxmlUtil.attachFXML(this, "/view/PortView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.setPickOnBounds(false);
        connectionNodeCircle.setCenterX(x);
        connectionNodeCircle.setCenterY(y);
        connectionNodeCircle.onMouseClickedProperty().setValue(e -> this.onClick());
        viewModel.onConnectionStateChanged().addListener(this::handleUpdatedConnectionState);
        this.viewModel = viewModel;
    }

    /* Public */
    public Position getPos() {
        Point2D pos = connectionNodeCircle.localToParent(new Point2D(
                connectionNodeCircle.getCenterX(),
                connectionNodeCircle.getCenterY()
        ));

        pos = this.localToParent(pos);
        pos = this.getParent().localToParent(pos);
        pos = this.getParent().getParent().localToParent(pos);
        return new Position((int) pos.getX(), (int) pos.getY());
    }

    /* Package-Private */
    void computeAndSetStyleClasses(Port port, String... extraClasses) {
        List<String> styleClasses = new ArrayList<>(Arrays.asList(extraClasses));
        if (port.isConnected()) {
            styleClasses.add("connected");
            if (port.isHigh()) {
                styleClasses.add("active");
            }
        } else if (viewModel.canConnectTo(port) && viewModel.wireHasConnections()) {
            styleClasses.add("candidate");
        }
        this.connectionNodeCircle.getStyleClass().setAll(styleClasses);
    }

    /* Private */
    protected abstract void onClick();

    protected abstract void handleUpdatedConnectionState();
}
