package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.flipflop.JkFlipFlopComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

public class JkFlipFlopComponentTest {
    @Test
    void intendedUseCase() {
        SignalSourceComponent jackSignal = new SignalSourceComponent();
        SignalSourceComponent kilbySignal = new SignalSourceComponent();
        SignalSourceComponent clkSignal = new SignalSourceComponent();

        Wire jackWire = new Wire();
        Wire kilbyWire = new Wire();
        Wire clkWire = new Wire();

        jackSignal.connectToPort(jackWire, "output");
        kilbySignal.connectToPort(kilbyWire, "output");
        clkSignal.connectToPort(clkWire, "output");

        JkFlipFlopComponent jkFlipFlop = new JkFlipFlopComponent();

        jkFlipFlop.connectToPort(jackWire, "jackInput");
        jkFlipFlop.connectToPort(kilbyWire, "kilbyInput");
        jkFlipFlop.connectToPort(clkWire, "clkInput");

        Wire outputWire = new Wire();
        jkFlipFlop.connectToPort(outputWire, "output");

        jackSignal.toggle(false);
        kilbySignal.toggle(true);
        kilbySignal.toggle(false);

        assertTrue(outputWire.isLow());

        jackSignal.toggle();
        assertTrue(outputWire.isHigh());

        jackSignal.toggle();
        assertTrue(outputWire.isHigh());

        clkSignal.toggle(false);
        jackSignal.toggle(true);
        kilbySignal.toggle(true);
        for (int i = 0; i < 10; i++) {
            assertTrue(outputWire.isHigh());
            clkSignal.toggle();
            clkSignal.toggle();
            assertTrue(outputWire.isLow());
            clkSignal.toggle();
            clkSignal.toggle();
        }
    }
}
