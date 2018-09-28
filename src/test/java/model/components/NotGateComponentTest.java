package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

class NotGateComponentTest {

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

}
