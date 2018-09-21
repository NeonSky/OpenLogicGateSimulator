package model.components;

import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerSourceComponentTest {

    @Test
    void metadataTest() {
        PowerSourceComponent comp = new PowerSourceComponent();
        assertEquals("PowerSource", comp.getAnsiName());
        assertEquals("Power Source", comp.getDisplayName());
        assertEquals("Emits an active signal to all connected outputs", comp.getDescription());
    }

    @Test
    void powerTest() {
        Wire wire = new Wire();
        PowerSourceComponent comp = new PowerSourceComponent();

        assertFalse(wire.isHigh());
        comp.connectToPort(wire, "output");
        assertTrue(wire.isHigh());
        comp.disconnectFromPort(wire, "output");
        assertFalse(wire.isHigh());
        comp.connectToPort(wire, "output");
        assertTrue(wire.isHigh());
    }
}
