package org.cafebabe.model.editor.workspace;

import org.cafebabe.model.editor.workspace.circuit.Circuit;

/**
 * Contains the circuit, should do more.
 */
public class Workspace {

    private final Circuit circuit;

    public Workspace() {
        this.circuit = new Circuit();
    }

    /* Public */
    public Circuit getCircuit() {
        return this.circuit;
    }

}
