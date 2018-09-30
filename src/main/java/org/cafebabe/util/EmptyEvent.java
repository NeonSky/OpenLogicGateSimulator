package org.cafebabe.util;

import java.util.HashSet;
import java.util.Set;


public class EmptyEvent {
    private final Set<Runnable> listeners = new HashSet<>();

    /* Public */

    /**
     * Adds listener to this event.
     */
    public void addListener(Runnable listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes listener from this event.
     */
    public void removeListener(Runnable listener) {
        this.listeners.remove(listener);
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
