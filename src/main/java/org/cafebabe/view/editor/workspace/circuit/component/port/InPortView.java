package org.cafebabe.view.editor.workspace.circuit.component.port;

import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.removemeplz.ViewModel;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortView extends PortView {

    private final InputPort port;

    public InPortView(InputPort port, double x, double y, ViewModel viewModel) {
        super(port, x, y, viewModel);
        this.port = port;
    }

    /* Public */
    @Override
    public InputPort getPort() {
        return this.port;
    }
}
