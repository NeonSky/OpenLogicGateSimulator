package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

class SignalSourceComponentTest {


    /* Package-Private */
    @Test
    void powerTest() {
        Wire wire = new Wire();
        SignalSourceComponent comp = new SignalSourceComponent();

        assertFalse(wire.isHigh());
        comp.connectToPort(wire, "output");
        assertTrue(wire.isHigh());
        comp.disconnectFromPort(wire, "output");
        assertFalse(wire.isHigh());
        comp.connectToPort(wire, "output");
        assertTrue(wire.isHigh());
    }
}
