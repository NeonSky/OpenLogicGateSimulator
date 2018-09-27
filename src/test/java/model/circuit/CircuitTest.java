package model.circuit;

import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.Component;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CircuitTest {

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
        assertEquals(c.getComponents().size(), 1);
    }

    @Test
    void addMultipleComponentsToCircuit() {
        Circuit c = new Circuit();

        for (int i=0; i < 10; i++) {
            c.addComponent(new AndGateComponent());
            assertEquals(c.getComponents().size(), i+1);
        }
    }

    @Test
    void addingSameComponentTwiceShouldThrow() {
        Circuit c = new Circuit();
        Component a = new AndGateComponent();

        c.addComponent(a);
        assertEquals(c.getComponents().size(), 1);  // adding once is fine
        assertThrows(RuntimeException.class, () -> c.addComponent(a));  // adding again is error
    }

    @Test
    void removeSingleComponentFromCircuit() {
        Circuit c = new Circuit();

        AndGateComponent a = new AndGateComponent();

        c.addComponent(a);
        assertEquals(c.getComponents().size(), 1);
        c.removeComponent(a);
        assertTrue(c.getComponents().isEmpty());
    }

    @Test
    void removeMultipleComponentsFromCircuit() {
        Circuit c = new Circuit();

        List<Component> componentList = new ArrayList<>();

        for (int i=0; i<10; i++) {
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

}