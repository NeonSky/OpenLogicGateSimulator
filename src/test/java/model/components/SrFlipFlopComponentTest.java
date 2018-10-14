package model.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.flipflop.SrFlipFlopComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;

public class SrFlipFlopComponentTest {
    @Test
    void intendedUseCase() {
        SignalSourceComponent setSignal = new SignalSourceComponent();
        SignalSourceComponent resetSignal = new SignalSourceComponent();

        Wire setWire = new Wire();
        Wire resetWire = new Wire();

        setSignal.connectToPort(setWire, "output");
        resetSignal.connectToPort(resetWire, "output");

        SrFlipFlopComponent srFlipFlop = new SrFlipFlopComponent();

        srFlipFlop.connectToPort(setWire, "setInput");
        srFlipFlop.connectToPort(resetWire, "resetInput");

        Wire outputWire = new Wire();
        srFlipFlop.connectToPort(outputWire, "output");

        setSignal.toggle(false);
        resetSignal.toggle(true);
        resetSignal.toggle(false);

        assertTrue(outputWire.isLow());

        setSignal.toggle();
        assertTrue(outputWire.isHigh());

        setSignal.toggle();
        assertTrue(outputWire.isHigh());

        setSignal.toggle(true);
        resetSignal.toggle(true);
        assertTrue(outputWire.isUndefined());
    }
}
