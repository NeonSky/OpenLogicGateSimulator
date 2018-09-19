package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.components.connections.IConnectionState;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.Event;

public abstract class PortController extends AnchorPane {

    final IWireConnector wireConnector;
    @FXML
    protected Circle connectionNodeCircle;
    private final String name;

    public PortController(String name, double x, double y, IWireConnector wireConnector) {
        this.name = name;
        FxmlUtil.attachFXML(this, "/view/PortView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        this.setPickOnBounds(false);
        connectionNodeCircle.setCenterX(x);
        connectionNodeCircle.setCenterY(y);
        connectionNodeCircle.onMouseClickedProperty().setValue(e -> this.onClick());
        wireConnector.addConnectionStateListener(this::handleUpdatedConnectionState);
        this.wireConnector = wireConnector;
    }

    protected abstract void onClick();

    protected abstract void handleUpdatedConnectionState(IConnectionState connectionState);

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
}
