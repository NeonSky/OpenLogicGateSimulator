package model.components;

import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotGateComponentTest {

    @Test
    void metadataTest() {
        NotGateComponent comp = new NotGateComponent();
        assertEquals("NOT_ANSI", comp.getAnsiName());
        assertEquals("NOT-Gate", comp.getDisplayName());
        assertEquals("Emits the opposite signal of the input", comp.getDescription());
    }

    @Test
    void intendedUseTest() {
        PowerSourceComponent power = new PowerSourceComponent();
        Wire in = new Wire();
        Wire out = new Wire();
        NotGateComponent comp = new NotGateComponent();

        power.connectToPort(in, "output");
        comp.connectToPort(in, "input");
        comp.connectToPort(out, "output");

        // Connect wires to power sources
        assertFalse(out.isHigh());
    }

    @Test
    void noSelfEnergyTest() {
        // TODO
    }
}
