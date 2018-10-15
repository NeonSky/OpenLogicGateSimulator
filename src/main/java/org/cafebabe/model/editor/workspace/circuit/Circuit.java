package org.cafebabe.model.editor.workspace.circuit;

import java.util.HashSet;
import java.util.Set;

import org.cafebabe.model.IModel;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.IDynamicComponent;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.simulation.Simulator;

/**
 * Owns the wires and component, and ensures that management of
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
            throw new ComponentAlreadyAddedException();
        }

        if (component instanceof IDynamicComponent) {
            ((IDynamicComponent) component).getOnNewDynamicEvent()
                    .addListenerWithOwner(this.simulator::addEvent, component);
            this.simulator.addEvents(((IDynamicComponent) component).getInitialDynamicEvents());
        }

        component.setEventScheduler(this.simulator);
        this.components.add(component);
    }

    public void removeComponent(Component component) {
        if (!this.components.contains(component)) {
            throw new ComponentNotInWorkspaceException();
        }

        if (component instanceof IDynamicComponent) {
            ((IDynamicComponent) component).getOnNewDynamicEvent()
                    .removeListenersWithOwner(component);
        }

        this.components.remove(component);
    }

    public void addWire(Wire wire) {
        if (this.wires.contains(wire)) {
            throw new WireAlreadyAddedException();
        }

        wire.setEventScheduler(this.simulator);
        this.wires.add(wire);
    }

    public void removeWire(Wire wire) {
        if (!this.wires.contains(wire)) {
            throw new WireNotInWorkspaceException();
        }

        this.wires.remove(wire);
    }

    public void removeItem(IModel item) {
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
