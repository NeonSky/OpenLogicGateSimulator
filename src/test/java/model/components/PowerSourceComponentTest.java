package model.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

class PowerSourceComponentTest {

    @Test
    void metadataTest() {
        PowerSourceComponent comp = new PowerSourceComponent();
        assertEquals("XOR_ANSI", comp.getAnsiName());
        assertEquals("Power Source", comp.getDisplayName());
        assertEquals("Emits an active signal to all connected outputs", comp.getDescription());
    }

    @Test
    void updateTest() {
        PowerSourceComponent comp = new PowerSourceComponent();
        comp.update();
    }

    @Test
    void powerTest() {
        Wire wire = new Wire();
        PowerSourceComponent comp = new PowerSourceComponent();

        assertEquals(false, wire.isActive());
        comp.connectToPort(wire, "north");
        assertEquals(true, wire.isActive());
        comp.disconnectFromPort(wire, "north");
        assertEquals(false, wire.isActive());
        comp.connectToPort(wire, "north");
        assertEquals(true, wire.isActive());
    }
}
