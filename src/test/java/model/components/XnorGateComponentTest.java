package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.XnorGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.PowerSourceComponent;
import org.junit.jupiter.api.Test;

class XnorGateComponentTest {


    @Test
    void intendedUseTest() {
        PowerSourceComponent power1 = new PowerSourceComponent();
        PowerSourceComponent power2 = new PowerSourceComponent();
        Wire in1 = new Wire();
        Wire in2 = new Wire();
        Wire out = new Wire();
        XnorGateComponent comp = new XnorGateComponent();

        // Connect wires to power source
        power1.connectToPort(in1, "output");
        power2.connectToPort(in2, "output");

        comp.connectToPort(out, "output");

        // XNOR-Gate output should only be 1 when amount of active
        // inputs is not equal to 1
        comp.connectToPort(in1, "input1");
        assertFalse(out.isHigh());
        comp.connectToPort(in2, "input2");
        assertTrue(out.isHigh());
    }

}
