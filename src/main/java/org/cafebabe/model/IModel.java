package org.cafebabe.model;

import org.cafebabe.util.EmptyEvent;

public interface IModel {
    void destroy();
    EmptyEvent onDestroyed();
}
