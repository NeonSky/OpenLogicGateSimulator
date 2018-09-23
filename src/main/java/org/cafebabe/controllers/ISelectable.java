package org.cafebabe.controllers;

public interface ISelectable {
    void select();
    void deselect();
    void disconnectFromWorkspace();
    IBelongToCircuit getModelObject();
}
