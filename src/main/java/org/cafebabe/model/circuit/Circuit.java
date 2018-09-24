package org.cafebabe.model.circuit;

import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;

import java.util.*;

public class Circuit {
    private Set<Component> components = new HashSet<>();
    private Set<Wire> wires = new HashSet<>();

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

    public void safeRemove(IBelongToCircuit component) {
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

    public void simulate() {} // TODO
}
