package org.cafebabe.model.editor.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


/**
 * A generic event.
 * @param <T> The type of data transmitted by the event.
 */
public class Event<T> {
    private final Set<OwnedConsumer<T>> listeners = new HashSet<>();

    /* Public */
    public void addListener(Consumer<T> listener) {
        this.listeners.add(new OwnedConsumer<>(listener, null));
    }

    public void addListenerWithOwner(Consumer<T> listener, Object owner) {
        this.listeners.add(new OwnedConsumer<>(listener, owner));
    }

    /* It is not possible to compare lambdas as each one is unique
       Instead we have to associate some events with an owner object.
     */
    public void removeListenersWithOwner(Object owner) {
        Set<OwnedConsumer> matches = new HashSet<>();

        for (OwnedConsumer listener : this.listeners) {
            if (owner.equals(listener.owner)) {
                matches.add(listener);
            }
        }
        this.listeners.removeAll(matches);
    }

    public void removeListeners() {
        this.listeners.clear();
    }

    public void notifyListeners(T arg) {
        this.listeners.forEach(x -> x.consumer.accept(arg));
    }
}
