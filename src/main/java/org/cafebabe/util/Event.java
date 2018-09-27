package org.cafebabe.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


public class Event<T> {
    private final Set<Consumer<T>> listeners = new HashSet<>();

    /* Public */
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }

    public void removeListeners() {
        listeners.clear();
    }

    public void notifyListeners(T arg) {
        listeners.forEach(x -> x.accept(arg));
    }

    public Set<Consumer<T>> listeners() {
        return listeners;
    }
}
