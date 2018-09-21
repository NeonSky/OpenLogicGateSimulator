package model.components;

import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AndGateComponentTest {

    @Test
    void metadataTest() {
        AndGateComponent comp = new AndGateComponent();
        assertEquals("AND_ANSI", comp.getAnsiName());
        assertEquals("AND-Gate", comp.getDisplayName());
        assertEquals("Emits an active signal if both inputs are active", comp.getDescription());
    }

    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        AndGateComponent comp = new AndGateComponent();

        // Connect wires to power sources
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        // initial state should off
        comp.connectToPort(out, "output");
        assertFalse(out.isActive());

        // AND-Gate output should only be 1 when both inputs are on
        comp.connectToPort(in1, "input1");
        assertFalse(out.isActive());
        comp.connectToPort(in2, "input2");
        assertTrue(out.isActive());

    }
}
