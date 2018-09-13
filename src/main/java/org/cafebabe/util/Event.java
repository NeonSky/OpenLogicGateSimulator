package org.cafebabe.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


public class Event<T> {
    private Set<Consumer<T>> listeners = new HashSet<>();

    /** Adds listener to this event */
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    /** Removes listener from this event */
    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }

    /** Calls all listener and provides the given argument */
    public void notifyAll(T arg) {
        listeners.forEach(x -> x.accept(arg));
    }
}
