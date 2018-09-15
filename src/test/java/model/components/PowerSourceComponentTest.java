package model.components;

import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerSourceComponentTest {

    @Test
    void metadataTest() {
        PowerSourceComponent comp = new PowerSourceComponent();
        assertEquals("XOR_ANSI", comp.getAnsiName());
        assertEquals("Power Source", comp.getDisplayName());
        assertEquals("Emits an active signal to all connected outputs", comp.getDescription());
    }

    @Test
    void powerTest() {
        Wire wire = new Wire();
        PowerSourceComponent comp = new PowerSourceComponent();

        assertFalse(wire.isActive());
        comp.connectToPort(wire, "north");
        assertTrue(wire.isActive());
        comp.disconnectFromPort(wire, "north");
        assertFalse(wire.isActive());
        comp.connectToPort(wire, "north");
        assertTrue(wire.isActive());
    }
}
