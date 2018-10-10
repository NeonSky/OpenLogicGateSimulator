package org.cafebabe.gui.editor.workspace.circuit.component.port;

import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.viewmodel.ViewModel;

/**
 * The "white circle" representing an output port of a component.
 */
public class OutPortController extends PortController {
    private final OutputPort port;

    public OutPortController(double x, double y, OutputPort port, ViewModel connectionManager) {
        super(x, y, port, connectionManager);
        this.port = port;
        updateStyleClasses("outPort");
    }

    /* Private */
    private void connectIfPossible() {
        this.viewModel.tryConnectWire(this.port);
    }

    /* Protected */
    @Override
    protected void onClick() {
        this.connectIfPossible();
    }

    @Override
    protected void handleUpdatedConnectionState() {
        updateStyleClasses("outPort");
    }
}
