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

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        output.setState(LogicState.HIGH);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertTrue(input.isHigh());

        output.setState(LogicState.LOW);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());
    }

    @Test
    void plugInPlugOutTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();
        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        output.setState(LogicState.HIGH);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertTrue(input.isHigh());

        wire.disconnectInputPort(input);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertFalse(input.isHigh());

        wire.disconnectOutputPort(output);

        assertTrue(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());
    }
}
