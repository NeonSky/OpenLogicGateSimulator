package org.cafebabe.model;

import org.cafebabe.model.editor.util.EmptyEvent;

public interface IModel {
    void destroy();
    EmptyEvent getOnDestroy();
}
