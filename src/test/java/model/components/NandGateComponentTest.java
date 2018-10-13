package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NandGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;


class NandGateComponentTest {


    /* Package-Private */
    @Test
    void oneHighInputShouldGiveHighOutput() {
        SignalSourceComponent power = new SignalSourceComponent();
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
        SignalSourceComponent power = new SignalSourceComponent();
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
        SignalSourceComponent power = new SignalSourceComponent();
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
