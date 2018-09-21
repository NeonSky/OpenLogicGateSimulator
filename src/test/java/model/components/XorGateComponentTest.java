package model.components;

import org.cafebabe.model.components.XorGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XorGateComponentTest {

    @Test
    void metadataTest() {
        XorGateComponent comp = new XorGateComponent();
        assertEquals("XOR_ANSI", comp.getAnsiName());
        assertEquals("XOR-Gate", comp.getDisplayName());
        assertEquals("Emits an active signal if exactly one input is active", comp.getDescription());
    }

    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        XorGateComponent comp = new XorGateComponent();

        // Connect wires to power sources
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        // initial state should off
        comp.connectToPort(out, "output");
        assertFalse(out.isActive());

        // XOR-Gate output should only be 1 when only one input is active
        comp.connectToPort(in1, "input1");
        assertTrue(out.isActive());
        comp.connectToPort(in2, "input2");
        assertFalse(out.isActive());

    }
}
