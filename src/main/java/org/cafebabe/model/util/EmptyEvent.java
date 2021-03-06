package org.cafebabe.model.util;

import java.util.HashSet;
import java.util.Set;


/**
 * An event that transmits no data, just informs listeners that something has happened.
 */
public class EmptyEvent {
    private final Set<Runnable> listeners = new HashSet<>();

    /* Public */

    /**
     * Adds listener to this event.
     */
    public void addListener(Runnable listener) {
        this.listeners.add(listener);
    }

    public void removeListeners() {
        this.listeners.clear();
    }

    /**
     * Calls all listener and provides the given argument.
     */
    public void notifyListeners() {
        this.listeners.forEach(Runnable::run);
    }
}
