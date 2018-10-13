package model.circuit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
class CircuitTest {

    /* Package-Private */
    @Test
    void newCircuitShouldBeEmpty() {
        Circuit c = new Circuit();
        assertTrue(c.getComponents().isEmpty());
        assertTrue(c.getWires().isEmpty());
    }

    @Test
    void addSingleComponentToCircuit() {
        Circuit c = new Circuit();
        c.addComponent(new AndGateComponent());
        assertEquals(1, c.getComponents().size());
    }

    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    void addMultipleComponentsToCircuit() {
        Circuit c = new Circuit();

        for (int i = 0; i < 10; i++) {
            c.addComponent(new AndGateComponent());
            assertEquals(i + 1, c.getComponents().size());
        }
    }

    @Test
    void addingSameComponentTwiceShouldThrow() {
        Circuit c = new Circuit();
        Component a = new AndGateComponent();

        c.addComponent(a);
        assertEquals(1, c.getComponents().size());  // adding once is fine
        assertThrows(RuntimeException.class, () -> c.addComponent(a));  // adding again is error
    }

    @Test
    void removeSingleComponentFromCircuit() {
        Circuit c = new Circuit();

        AndGateComponent a = new AndGateComponent();

        c.addComponent(a);
        assertEquals(1, c.getComponents().size());
        c.removeComponent(a);
        assertTrue(c.getComponents().isEmpty());
    }

    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    void removeMultipleComponentsFromCircuit() {
        Circuit c = new Circuit();

        List<Component> componentList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AndGateComponent a = new AndGateComponent();
            c.addComponent(a);
            componentList.add(a);
        }

        for (Component component : componentList) {
            c.removeComponent(component);
        }

        assertTrue(c.getComponents().isEmpty());
    }

    @Test
    void removeNonexistentComponentShouldThrow() {
        Circuit c = new Circuit();
        assertThrows(RuntimeException.class, () -> c.removeComponent(new AndGateComponent()));
    }

    @Test
    void addSingleWireToCircuit() {
        Circuit c = new Circuit();
        c.addWire(new Wire());
        assertEquals(1, c.getWires().size());
    }

    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    void addMultipleWireToCircuit() {
        Circuit c = new Circuit();

        for (int i = 0; i < 10; i++) {
            c.addWire(new Wire());
            assertEquals(i + 1, c.getWires().size());
        }
    }

    @Test
    void addSameWireShouldThrow() {
        Circuit c = new Circuit();
        Wire w = new Wire();

        c.addWire(w);
        assertEquals(1, c.getWires().size());  // adding once is fine
        assertThrows(RuntimeException.class, () -> c.addWire(w));  // adding again is error
    }

    @Test
    void removeSingleWireFromCircuit() {
        Circuit c = new Circuit();
        Wire w = new Wire();

        c.addWire(w);
        assertEquals(1, c.getWires().size());
        c.removeWire(w);
        assertTrue(c.getWires().isEmpty());
    }

    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    void removeMultipleWiresFromCircuit() {
        Circuit c = new Circuit();

        List<Wire> wireList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Wire w = new Wire();
            c.addWire(w);
            wireList.add(w);
        }

        for (Wire w : wireList) {
            c.removeWire(w);
        }

        assertTrue(c.getComponents().isEmpty());
    }

    @Test
    void removeNonexistentWireShouldThrow() {
        Circuit c = new Circuit();
        assertThrows(RuntimeException.class, () -> c.removeWire(new Wire()));
    }

    @Test
    void removeItemShouldRemoveComponent() {
        Circuit c = new Circuit();
        Component component = new AndGateComponent();
        c.addComponent(component);
        assertEquals(1, c.getComponents().size());

        c.removeComponent(component);
        assertEquals(0, c.getComponents().size());
    }

    @Test
    void removeItemShouldRemoveWire() {
        Circuit c = new Circuit();
        Wire wire = new Wire();
        c.addWire(wire);
        assertEquals(1, c.getWires().size());

        c.removeWire(wire);
        assertEquals(0, c.getWires().size());
    }

}