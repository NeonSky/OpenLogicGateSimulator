package org.cafebabe.model.components.connections;

import org.cafebabe.model.circuit.IBelongToCircuit;

public abstract class Port extends LogicStateContainer implements IBelongToCircuit {

    public abstract boolean isConnected();

}
