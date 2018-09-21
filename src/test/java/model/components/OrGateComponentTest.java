package model.components;

import org.cafebabe.model.components.OrGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrGateComponentTest {

    @Test
    void metadataTest() {
        OrGateComponent comp = new OrGateComponent();
        assertEquals("OR_ANSI", comp.getAnsiName());
        assertEquals("OR-Gate", comp.getDisplayName());
        assertEquals("Emits an active signal if either inputs are active", comp.getDescription());
    }

    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        OrGateComponent comp = new OrGateComponent();

        // Connect wires to power sources
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        // initial state should off
        comp.connectToPort(out, "output");
        assertFalse(out.isActive());

        // OR-Gate output should be 1 when either inputs are on
        comp.connectToPort(in1, "input1");
        assertTrue(out.isActive());
        comp.connectToPort(in2, "input2");
        assertTrue(out.isActive());

    }
}
