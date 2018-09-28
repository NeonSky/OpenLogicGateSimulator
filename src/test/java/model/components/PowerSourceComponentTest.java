package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

class PowerSourceComponentTest {


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
