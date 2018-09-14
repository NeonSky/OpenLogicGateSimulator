package org.cafebabe.model.circuit;

import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;

import java.util.*;

public class Circuit {
    private Set<Component> componentList = new HashSet<>();
    private Set<Wire> wires = new HashSet<>();

    public void addComponent(Component component) {
        this.addComponent(component, 0, 0);
    }

    public void addComponent(Component component, int x, int y) {
        if (this.componentList.contains(component)) {
            throw new RuntimeException("Trying to add same component to workspace several times");
        }

        this.componentList.add(component);
    }

    public void removeComponent(Component component) {
        if (!this.componentList.contains(component)) {
            throw new RuntimeException("Trying to remove nonexistent component from workspace");
        }

        this.componentList.remove(component);
    }

    public void addWire(Wire wire) {
        this.wires.add(wire);
    }

    public void removeWire(Wire wire) {
        this.wires.remove(wire);
    }

    public void simulate() {} // TODO
}
