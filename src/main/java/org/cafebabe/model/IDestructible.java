package org.cafebabe.model;

import org.cafebabe.util.EmptyEvent;

public interface IDestructible {
    /* Public */
    void destroy();

    EmptyEvent getOnDestroy();
}
