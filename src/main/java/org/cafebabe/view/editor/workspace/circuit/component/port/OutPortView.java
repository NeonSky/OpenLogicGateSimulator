package org.cafebabe.view.editor.workspace.circuit.component.port;

import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.removemeplz.ViewModel;

/**
 * The "white circle" representing an output port of a component.
 */
public class OutPortView extends PortView {

    private final OutputPort port;

    public OutPortView(OutputPort port, double x, double y, ViewModel viewModel) {
        super(port, x, y, viewModel);
        this.port = port;
    }

    /* Public */
    @Override
    public OutputPort getPort() {
        return this.port;
    }
}
