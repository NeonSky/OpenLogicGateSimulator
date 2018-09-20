package model.components;

import org.cafebabe.model.components.NandGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NandGateComponentTest {

    @Test
    void metadataTest() {
        NandGateComponent comp = new NandGateComponent();
        assertEquals("NAND_ANSI", comp.getAnsiName());
        assertEquals("NAND-Gate", comp.getDisplayName());
        assertEquals("Emits no signal if both inputs are active", comp.getDescription());
    }

    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        NandGateComponent comp = new NandGateComponent();

        // Connect wires to power sources
        power1.connectToPort(in1, "north");
        power2.connectToPort(in2, "north");

        comp.connectToPort(out, "output");

        // NAND-Gate output should only be 0 both inputs are on
        comp.connectToPort(in1, "input1");
        assertTrue(out.isActive());
        comp.connectToPort(in2, "input2");
        assertFalse(out.isActive());
    }

    @Test
    void noSelfEnergyTest() {
        // TODO
    }
}
