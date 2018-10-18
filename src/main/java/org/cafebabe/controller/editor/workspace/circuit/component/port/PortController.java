package org.cafebabe.controller.editor.workspace.circuit.component.port;

import org.cafebabe.controller.Controller;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.view.editor.workspace.circuit.component.port.PortView;

/**
 * Handles user interactions with the port view.
 */
public abstract class PortController extends Controller {

    protected final PortView view;
    private final Port port;
    private boolean destructionPending;


    PortController(PortView view) {
        super(view);
        this.view = view;

        this.port = view.getPort();
        this.port.getOnDestroy().addListener(this::destroy);
        this.port.getOnStateChanged().addListener((p) -> this.handleUpdatedConnectionState());
    }

    /* Public */
    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.port.destroy();
        }
    }

    /* Protected */
    protected void updateStyleClasses() {
        this.view.updateStyleClasses();
    }

    protected abstract void handleUpdatedConnectionState();

}
