package model.components.connections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.cafebabe.model.components.*;
import org.cafebabe.model.components.connections.*;

public class WireTest {

    @Test
    void powerTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();

        assertEquals(false, output.isActive());
        assertEquals(false, wire.isActive());
        assertEquals(false, input.isActive());

        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertEquals(false, output.isActive());
        assertEquals(false, wire.isActive());
        assertEquals(false, input.isActive());

        output.setActive(true);

        assertEquals(true, output.isActive());
        assertEquals(true, wire.isActive());
        assertEquals(true, input.isActive());

        output.setActive(false);

        assertEquals(false, output.isActive());
        assertEquals(false, wire.isActive());
        assertEquals(false, input.isActive());
    }

    @Test
    void plugInPlugOutTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();
        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertEquals(false, output.isActive());
        assertEquals(false, wire.isActive());
        assertEquals(false, input.isActive());

        output.setActive(true);

        assertEquals(true, output.isActive());
        assertEquals(true, wire.isActive());
        assertEquals(true, input.isActive());

        wire.disconnectInputPort(input);

        assertEquals(true, output.isActive());
        assertEquals(true, wire.isActive());
        assertEquals(false, input.isActive());

        wire.disconnectOutputPort(output);

        assertEquals(true, output.isActive());
        assertEquals(false, wire.isActive());
        assertEquals(false, input.isActive());
    }
}
