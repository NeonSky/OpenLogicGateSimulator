package model.components;

import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.XorGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XorGateComponentTest {

    private static Wire on, off;

    @Test
    void metadataTest() {
        XorGateComponent comp = new XorGateComponent();
        assertEquals("XOR_ANSI", comp.getAnsiName());
        assertEquals("XOR-Gate", comp.getDisplayName());
        assertEquals("Emits an active signal if exactly one input is active", comp.getDescription());
    }

    @BeforeAll
    static void setup() {
        PowerSourceComponent power = new PowerSourceComponent();
        NotGateComponent not = new NotGateComponent();
        on = new Wire();
        off = new Wire();

        power.connectToPort(on, "output");
        not.connectToPort(on, "input");
        not.connectToPort(off, "output");
    }

    @Test
    void highInputsShouldGiveLowOutput() {
        XorGateComponent comp = new XorGateComponent();
        Wire res = new Wire();

        comp.connectToPort(on, "input1");
        comp.connectToPort(on, "input2");
        comp.connectToPort(res, "output");

        // XOR-Gate output should only be 1 when only one input is active
        assertTrue(comp.getPort("output").isLow());
        assertTrue(res.isLow());
    }

    @Test
    void lowInputsShouldGiveLowOutput() {
        XorGateComponent comp = new XorGateComponent();
        Wire res = new Wire();

        comp.connectToPort(off, "input1");
        comp.connectToPort(off, "input2");
        comp.connectToPort(res, "output");

        // XOR-Gate output should only be 1 when only one input is active
        assertTrue(res.isLow());
    }

    @Test
    void oneHighInputShouldGiveHighOutput() {
        XorGateComponent comp = new XorGateComponent();
        Wire res = new Wire();

        comp.connectToPort(on, "input1");
        comp.connectToPort(off, "input2");
        comp.connectToPort(res, "output");

        // XOR-Gate output should only be 1 when only one input is active
        assertTrue(res.isHigh());
    }

    @Test
    void oneHighInput2ShouldGiveHighOutput() {
        XorGateComponent comp = new XorGateComponent();
        Wire res = new Wire();

        comp.connectToPort(off, "input1");
        comp.connectToPort(on, "input2");
        comp.connectToPort(res, "output");

        // XOR-Gate output should only be 1 when only one input is active
        assertTrue(res.isHigh());
    }
}
