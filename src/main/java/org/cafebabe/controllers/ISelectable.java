package org.cafebabe.controllers;

import org.cafebabe.model.circuit.IBelongToModel;

public interface ISelectable {
    void select();
    void deselect();
    void disconnectFromWorkspace();
    IBelongToModel getModelObject();
}
