package org.cafebabe.gui.editor.workspace.circuit.wire;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import org.cafebabe.gui.IController;
import org.cafebabe.model.components.connections.LogicStateContainer;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.viewmodel.ISelectable;
import org.cafebabe.viewmodel.ITransformable;


/**
 * Provides a Wire that visually connects two ports in the circuit.
 * It is colored to reflect its current logic state.
 */
public class WireController implements IController, ISelectable, ITransformable {

    private final WireView view;
    private final Wire wire;


    public WireController(Wire wire) {
        this.wire = wire;
        this.view = new WireView(this.wire);

        this.wire.onDestroyed().addListener(this::onModelDestroyed);
        this.wire.onStateChangedEvent().addListener(this::updateVisualState);
        this.wire.onStartPosMoved.addListener(this.view::moveStartPointTo);
        this.wire.onEndPosMoved.addListener(this.view::moveEndPointTo);
    }

    /* Public */
    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.view.addClickListener(listener);
    }

    @Override
    public Node getView() {
        return this.view.getFxView();
    }

    @Override
    public void select() {
        this.view.setSelected(true);
        this.view.updateVisualState();
    }

    @Override
    public void deselect() {
        this.view.setSelected(false);
        this.view.updateVisualState();
    }

    @Override
    public void destroy() {
        this.wire.destroy();
    }

    @Override
    public void setTransform(Transform transform) {
        this.view.setTransform(transform);
    }

    /* Private */
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void updateVisualState(LogicStateContainer wire) {
        this.view.updateVisualState();
    }

    private void onModelDestroyed() {
        this.view.destroy();
    }

}
