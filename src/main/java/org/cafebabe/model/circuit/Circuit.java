package org.cafebabe.model.circuit;

import java.util.HashSet;
import java.util.Set;

import org.cafebabe.model.IDynamicComponent;
import org.cafebabe.model.circuit.simulation.Simulator;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;

/**
 * Owns the wires and components, and ensures that management of
 * them are handled through this, single interface.
 */
public class Circuit {

    private final Simulator simulator = new Simulator();
    private final Set<Component> components = new HashSet<>();
    private final Set<Wire> wires = new HashSet<>();


    public Circuit() {
        this.simulator.start();
    }

    /* Public */
    public void addComponent(Component component) {
        if (this.components.contains(component)) {
            throw new RuntimeException("Trying to add same component to workspace several times");
        }

        if (component instanceof IDynamicComponent) {
            this.simulator.addEvents(((IDynamicComponent) component).getInitialDynamicEvents());
        }

        component.setEventScheduler(this.simulator);
        this.components.add(component);
    }

    public void removeComponent(Component component) {
        if (!this.components.contains(component)) {
            throw new RuntimeException("Trying to remove nonexistent component from workspace");
        }

        this.components.remove(component);
    }

    public void addWire(Wire wire) {
        if (this.wires.contains(wire)) {
            throw new RuntimeException("Trying to add same wire to workspace several times");
        }

        wire.setEventScheduler(this.simulator);
        this.wires.add(wire);
    }

    public void removeWire(Wire wire) {
        if (!this.wires.contains(wire)) {
            throw new RuntimeException("Trying to remove nonexistent wire from workspace");
        }

        this.wires.remove(wire);
    }

    public void removeItem(IBelongToModel item) {
        if (item instanceof Component) {
            removeComponent((Component) item);
        }
        if (item instanceof Wire) {
            removeWire((Wire) item);
        }
    }

    public Set<Component> getComponents() {
        return this.components;
    }

    public Set<Wire> getWires() {
        return this.wires;
    }

}
