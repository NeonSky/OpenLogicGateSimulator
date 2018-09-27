package org.cafebabe.viewmodel;

import org.cafebabe.model.circuit.IBelongToModel;

public interface ISelectable {
    /* Public */
    void select();

    void deselect();

    void disconnectFromWorkspace();

    void destroy();

    IBelongToModel getModelObject();
}
