package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;


class AndGateComponentTest {


    /* Package-Private */
    @Test
    void intendedUseTest() {
        SignalSourceComponent power1 = new SignalSourceComponent();
        SignalSourceComponent power2 = new SignalSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        AndGateComponent comp = new AndGateComponent();

        // Connect wires to power source
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        // initial state should off
        comp.connectToPort(out, "output");
        assertFalse(out.isHigh());

        // AND-Gate output should only be 1 when both inputs are on
        comp.connectToPort(in1, "input1");
        assertFalse(out.isHigh());
        comp.connectToPort(in2, "input2");
        assertTrue(out.isHigh());

    }
}
