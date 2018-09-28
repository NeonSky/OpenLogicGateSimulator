package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.components.NandGateComponent;
import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;


class NandGateComponentTest {


    @Test
    void oneHighInputShouldGiveHighOutput() {
        PowerSourceComponent power = new PowerSourceComponent();
        Wire on = new Wire();
        power.connectToPort(on, "output");

        NotGateComponent not = new NotGateComponent();
        not.connectToPort(on, "input");
        Wire off = new Wire();
        not.connectToPort(off, "output");

        NandGateComponent comp = new NandGateComponent();
        Wire res = new Wire();
        comp.connectToPort(res, "output");

        comp.connectToPort(on, "input1");
        comp.connectToPort(off, "input2");

        assertTrue(res.isHigh());
    }

    @Test
    void highInputsShouldGiveLowOutput() {
        PowerSourceComponent power = new PowerSourceComponent();
        Wire on = new Wire();
        power.connectToPort(on, "output");

        NandGateComponent comp = new NandGateComponent();
        Wire res = new Wire();
        comp.connectToPort(res, "output");

        comp.connectToPort(on, "input1");
        comp.connectToPort(on, "input2");

        assertTrue(res.isLow());
    }

    @Test
    void noHighInputsShouldGiveHighOutput() {
        PowerSourceComponent power = new PowerSourceComponent();
        Wire on = new Wire();
        power.connectToPort(on, "output");

        NotGateComponent not = new NotGateComponent();
        Wire off = new Wire();

        not.connectToPort(on, "input");
        not.connectToPort(off, "output");

        NandGateComponent comp = new NandGateComponent();
        Wire res = new Wire();
        comp.connectToPort(res, "output");

        comp.connectToPort(off, "input1");
        comp.connectToPort(off, "input2");

        assertTrue(res.isHigh());
    }

    @Test
    void noSelfEnergyTest() {
        NandGateComponent comp = new NandGateComponent();
        Wire res = new Wire();
        comp.connectToPort(res, "output");
        assertTrue(res.isUndefined());
    }
}
