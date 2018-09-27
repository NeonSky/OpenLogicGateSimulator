package org.cafebabe.model.circuit;

import java.util.HashSet;
import java.util.Set;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;

public class Circuit {
    private final Set<Component> components = new HashSet<>();
    private final Set<Wire> wires = new HashSet<>();

    /* Public */
    public void addComponent(Component component) {
        if (this.components.contains(component)) {
            throw new RuntimeException("Trying to add same component to workspace several times");
        }

        this.components.add(component);
    }

    public void removeComponent(Component component) {
        if (!this.components.contains(component)) {
            throw new RuntimeException("Trying to remove nonexistent component from workspace");
        }

        this.components.remove(component);
    }

    public void addWire(Wire wire) {
        this.wires.add(wire);
    }

    public void removeWire(Wire wire) {
        this.wires.remove(wire);
    }

    public void safeRemove(IBelongToModel component) {
        if (components.contains(component)) {
            this.components.remove(component);
        }
        if (wires.contains(component)) {
            this.components.remove(component);
        }
    }

    public Set<Component> getComponents() {
        return components;
    }

    public Set<Wire> getWires() {
        return wires;
    }

}
