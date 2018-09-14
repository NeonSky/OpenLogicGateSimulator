package org.cafebabe.model.workspace;

import org.cafebabe.model.circuit.Circuit;

public class Workspace {

    private final Circuit circuit;

    public Workspace() {
        this.circuit = new Circuit();
    }

    public Circuit getCircuit() {
        return this.circuit;
    }

}
