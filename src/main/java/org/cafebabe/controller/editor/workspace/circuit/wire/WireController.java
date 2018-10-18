package org.cafebabe.controller.editor.workspace.circuit.wire;

import org.cafebabe.controller.Controller;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;


/**
 * Handles user interactions with the wire view.
 */
public class WireController extends Controller {

    private final WireView view;
    private final Wire wire;
    private boolean destructionPending;


    public WireController(WireView view) {
        super(view);
        this.view = view;
        this.wire = view.getWire();

        this.wire.getOnDestroy().addListener(this::destroy);
        this.wire.getOnStateChanged().addListener((w) -> this.view.updateVisualState());
        this.wire.getOnStartPosMoved().addListener(this.view::moveStartPointTo);
        this.wire.getOnEndPosMoved().addListener(this.view::moveEndPointTo);

        this.view.updateVisualState();
    }

    /* Public */
    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.wire.destroy();
        }
    }

}
