package org.cafebabe.view.editor.workspace.circuit.component.port;

import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;

/**
 * The "white circle" representing an output port of a component.
 */
public class OutPortView extends PortView {

    private final OutputPort port;

    public OutPortView(OutputPort port, double x, double y) {
        super(port, x, y);
        this.port = port;
    }

    /* Public */
    @Override
    public OutputPort getPort() {
        return this.port;
    }
}
