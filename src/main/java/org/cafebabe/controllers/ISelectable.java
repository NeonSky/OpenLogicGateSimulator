package org.cafebabe.controllers;

import org.cafebabe.model.circuit.IBelongToCircuit;

public interface ISelectable {
    void select();
    void deselect();
    void disconnectFromWorkspace();
    IBelongToCircuit getModelObject();
}
