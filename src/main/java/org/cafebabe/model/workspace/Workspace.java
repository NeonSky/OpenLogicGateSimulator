package org.cafebabe.model.workspace;

import org.cafebabe.model.circuit.Circuit;

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
