package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

class NotGateComponentTest {

    /* Package-Private */
    @Test
    void intendedUseTest() {
        SignalSourceComponent power = new SignalSourceComponent();
        Wire in = new Wire();
        Wire out = new Wire();
        NotGateComponent comp = new NotGateComponent();

        power.connectToPort(in, "output");
        comp.connectToPort(in, "input");
        comp.connectToPort(out, "output");

        // Connect wires to power source
        assertFalse(out.isHigh());
    }

}
