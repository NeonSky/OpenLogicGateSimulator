package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.gate.OrGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

class OrGateComponentTest {


    /* Package-Private */
    @Test
    void intendedUseTest() {
        SignalSourceComponent power = new SignalSourceComponent();
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
