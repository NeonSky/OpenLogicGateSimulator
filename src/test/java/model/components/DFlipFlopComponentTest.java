package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.flipflop.DFlipFlopComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

public class DFlipFlopComponentTest {
    @Test
    void intendedUseTest() {
        SignalSourceComponent dataSignal = new SignalSourceComponent();
        SignalSourceComponent enableSignal = new SignalSourceComponent();

        Wire dataWire = new Wire();
        Wire enableWire = new Wire();

        dataSignal.connectToPort(dataWire, "output");
        enableSignal.connectToPort(enableWire, "output");

        DFlipFlopComponent dff = new DFlipFlopComponent();

        dff.connectToPort(dataWire, "dataInput");
        dff.connectToPort(enableWire, "enableInput");

        Wire outputWire = new Wire();
        dff.connectToPort(outputWire, "output");

        enableSignal.toggle(true);
        for (int i = 1; i < 10; i++) {
            dataSignal.toggle();
            assertTrue(outputWire.isLow());
            dataSignal.toggle();
            assertTrue(outputWire.isHigh());
        }
        enableSignal.toggle();
        dataSignal.toggle();
        assertTrue(outputWire.isHigh());
        enableSignal.toggle();
        assertTrue(outputWire.isLow());
    }
}
