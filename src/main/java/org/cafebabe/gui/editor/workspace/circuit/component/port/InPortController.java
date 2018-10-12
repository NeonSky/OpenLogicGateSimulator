package org.cafebabe.gui.editor.workspace.circuit.component.port;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.viewmodel.ViewModel;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortController extends PortController {
    private final InputPort port;

    public InPortController(double x, double y, InputPort port, ViewModel viewModel) {
        super(x, y, port, viewModel);
        this.port = port;
        updateStyleClasses("inPort");
    }

    /* Private */
    private void connectIfPossible() {
        viewModel.tryConnectWire(this.port);
    }

    /* Protected */
    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState() {
        updateStyleClasses("inPort");
    }

    @Override
    public void destroy() {
        this.port.destroy();
    }
}
