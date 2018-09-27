package org.cafebabe.model.components.connections;

import org.cafebabe.model.circuit.IBelongToModel;

public abstract class Port extends LogicStateContainer implements IBelongToModel {

    public abstract boolean isConnected();

}
