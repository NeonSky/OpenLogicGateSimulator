package org.cafebabe.view.editor.workspace.circuit.component.port;

import lombok.Getter;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * The "white circle" representing an input port of a component.
 */
public class InPortView extends PortView {

    @Getter private final InputPort port;

    public InPortView(InputPort port, double x, double y) {
        super(port, x, y);
        this.port = port;
    }
}
