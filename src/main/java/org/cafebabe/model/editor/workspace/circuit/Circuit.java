package org.cafebabe.model.editor.workspace.circuit;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

import lombok.Getter;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.IDynamicComponent;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.simulation.SimulationState;
import org.cafebabe.model.editor.workspace.circuit.simulation.Simulator;
import org.cafebabe.model.util.Event;

/**
 * Owns the wires and component, and ensures that management of
 * them are handled through this, single interface.
 */
public class Circuit {

    @Getter private final Event<Component> onComponentAdded = new Event<>();
    @Getter private final Event<Wire> onWireAdded = new Event<>();

    private final Simulator simulator = new Simulator();
    @Getter private final Set<Component> components = new LinkedHashSet<>();
    @Getter private final Set<Wire> wires = new LinkedHashSet<>();

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
        component.getOnDestroy().addListener(() -> this.components.remove(component));
        this.components.add(component);
        this.onComponentAdded.notifyListeners(component);
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
        wire.getOnDestroy().addListener(() -> this.wires.remove(wire));
        this.wires.add(wire);
        this.onWireAdded.notifyListeners(wire);
    }

    public void removeWire(Wire wire) {
        if (!this.wires.contains(wire)) {
            throw new WireNotInWorkspaceException();
        }

        this.wires.remove(wire);
    }

    public void toggleSimulationState() {
        this.simulator.toggleSimulationState();
    }

    public void stopSimulator() {
        this.simulator.stop();
    }

    public void registerSimulationStateListener(Consumer<SimulationState> listener) {
        this.simulator.registerSimulationStateListener(listener);
    }

    public boolean isEmpty() {
        return this.components.isEmpty() && this.wires.isEmpty();
    }
}
