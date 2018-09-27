package model.components;

import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.OrGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrGateComponentTest {


    @Test
    void intendedUseTest() {
        PowerSourceComponent power = new PowerSourceComponent();
        NotGateComponent not = new NotGateComponent();
        Wire on = new Wire();
        Wire off = new Wire();

        power.connectToPort(on, "output");
        not.connectToPort(on, "input");
        not.connectToPort(off, "output");

        // initially undefined
        OrGateComponent comp = new OrGateComponent();
        Wire res = new Wire();
        comp.connectToPort(res, "output");
        assertTrue(res.isUndefined());

        // OR-Gate output should be 1 when either inputs are on
        comp.connectToPort(off, "input1");
        comp.connectToPort(on, "input2");
        assertTrue(res.isHigh());

    }
}
