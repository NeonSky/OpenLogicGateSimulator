package org.cafebabe.model;

import org.cafebabe.model.util.EmptyEvent;

public interface IModel {
    void destroy();
    EmptyEvent onDestroyed();
}
