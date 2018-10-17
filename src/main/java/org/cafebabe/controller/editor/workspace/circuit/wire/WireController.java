package org.cafebabe.controller.editor.workspace.circuit.wire;

import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import org.cafebabe.controller.Controller;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.selection.ISelectable;
import org.cafebabe.model.editor.workspace.camera.IHaveTransform;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;


/**
 * Handles user interactions with the wire view.
 */
public class WireController extends Controller implements ISelectable, IHaveTransform {

    private final WireView view;
    private final Wire wire;
    private boolean destructionPending;


    public WireController(WireView view) {
        super(view);
        this.view = view;
        this.wire = view.getWire();
        ViewModel viewModel = view.viewModel;

        this.view.getWireLine().addEventFilter(MouseEvent.MOUSE_CLICKED, (e) ->
                viewModel.handleControllerClick(this, e)
        );
        viewModel.addTransform(this.view::setTransform);

        this.wire.getOnDestroy().addListener(this::destroy);
        this.wire.onStateChangedEvent().addListener((w) -> this.view.updateVisualState());
        this.wire.onStartPosMoved.addListener(this.view::moveStartPointTo);
        this.wire.onEndPosMoved.addListener(this.view::moveEndPointTo);

        this.view.updateVisualState();
    }

    /* Public */
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
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.view.viewModel.removeWire(this.wire);
            this.wire.destroy();
        }
    }

    @Override
    public void setTransform(Transform transform) {
        this.view.setTransform(transform);
    }

}
