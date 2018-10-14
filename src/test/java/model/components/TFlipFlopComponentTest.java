package model.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.flipflop.TFlipFlopComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

public class TFlipFlopComponentTest {
    @Test
    void intendedUseTest() {
        SignalSourceComponent power1 = new SignalSourceComponent();
        SignalSourceComponent power2 = new SignalSourceComponent();
        Wire toggleWire = new Wire();
        Wire clkWire = new Wire();
        Wire outputWire = new Wire();
        TFlipFlopComponent comp = new TFlipFlopComponent();

        // Connect wires to power sources
        power1.connectToPort(toggleWire, "output");
        power2.connectToPort(clkWire, "output");

        // initial state should off
        comp.connectToPort(outputWire, "output");
        assertFalse(outputWire.isHigh());

        // AND-Gate output should only be 1 when both inputs are on
        comp.connectToPort(toggleWire, "toggleInput");
        assertFalse(outputWire.isHigh());
        comp.connectToPort(clkWire, "clkInput");
        assertTrue(outputWire.isHigh());
        for (int i = 0; i < 10; i++) {
            power2.toggle();
            assertTrue(outputWire.isHigh());
            power2.toggle();
            power2.toggle();
            assertTrue(outputWire.isLow());
            power2.toggle();
        }
        power1.toggle();
        for (int i = 0; i < 10; i++) {
            power2.toggle();
            assertTrue(outputWire.isHigh());
        }

    }
}
