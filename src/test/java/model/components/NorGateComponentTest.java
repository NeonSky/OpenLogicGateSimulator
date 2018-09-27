package model.components;

import org.cafebabe.model.components.NorGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NorGateComponentTest {


    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        NorGateComponent comp = new NorGateComponent();

        // Connect wires to power sources
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        comp.connectToPort(out, "output");

        // NOR-Gate output should be 0 when either inputs are on
        comp.connectToPort(in1, "input1");
        assertFalse(out.isHigh());
        comp.connectToPort(in2, "input2");
        assertFalse(out.isHigh());

    }

}
