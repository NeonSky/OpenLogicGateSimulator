package org.cafebabe.util;

import java.util.HashSet;
import java.util.Set;


public class EmptyEvent {
    private final Set<Runnable> listeners = new HashSet<>();

    /* Public */

    /**
     * Adds listener to this event
     */
    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    /**
     * Removes listener from this event
     */
    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    public void removeListeners() {
        listeners.clear();
    }

    /**
     * Calls all listener and provides the given argument
     */
    public void notifyListeners() {
        listeners.forEach(Runnable::run);
    }
}
