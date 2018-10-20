package org.cafebabe.controller.editor.workspace.circuit.wire;

import org.cafebabe.controller.Controller;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;


/**
 * Handles user interactions with the wire view.
 */
public class WireController extends Controller {

    private final Wire wire;
    private boolean destructionPending;


    public WireController(WireView view) {
        super(view);
        this.wire = view.getWire();

        view.setPickOnBounds(false);
        view.getWireLine().setPickOnBounds(false);
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
