package model.components.connections;

import org.junit.jupiter.api.Test;

import org.cafebabe.model.components.connections.*;

import static org.junit.jupiter.api.Assertions.*;

class WireTest {

    @Test
    void powerTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();

        assertFalse(output.isActive());
        assertFalse(wire.isActive());
        assertFalse(input.isActive());

        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isActive());
        assertFalse(wire.isActive());
        assertFalse(input.isActive());

        output.setActive(true);

        assertTrue(output.isActive());
        assertTrue(wire.isActive());
        assertTrue(input.isActive());

        output.setActive(false);

        assertFalse(output.isActive());
        assertFalse(wire.isActive());
        assertFalse(input.isActive());
    }

    @Test
    void plugInPlugOutTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();
        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isActive());
        assertFalse(wire.isActive());
        assertFalse(input.isActive());

        output.setActive(true);

        assertTrue(output.isActive());
        assertTrue(wire.isActive());
        assertTrue(input.isActive());

        wire.disconnectInputPort(input);

        assertTrue(output.isActive());
        assertTrue(wire.isActive());
        assertFalse(input.isActive());

        wire.disconnectOutputPort(output);

        assertTrue(output.isActive());
        assertFalse(wire.isActive());
        assertFalse(input.isActive());
    }
}
