package model.components.connections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.PowerSourceComponent;
import org.junit.jupiter.api.Test;

class WireTest {

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void powerTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        output.setState(LogicState.HIGH);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertTrue(input.isHigh());

        output.setState(LogicState.LOW);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void plugInPlugOutTest() {
        Wire wire = new Wire();
        OutputPort output = new OutputPort();
        InputPort input = new InputPort();
        wire.connectOutputPort(output);
        wire.connectInputPort(input);

        assertFalse(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());

        output.setState(LogicState.HIGH);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertTrue(input.isHigh());

        wire.disconnectInputPort(input);

        assertTrue(output.isHigh());
        assertTrue(wire.isHigh());
        assertFalse(input.isHigh());

        wire.disconnectOutputPort(output);

        assertTrue(output.isHigh());
        assertFalse(wire.isHigh());
        assertFalse(input.isHigh());
    }


    @Test
    @SuppressWarnings(
            {"checkstyle:variabledeclarationusagedistance",
                    "PMD.JUnitTestContainsTooManyAsserts"})
    void shouldWorkWithNotChain() {
        PowerSourceComponent p = new PowerSourceComponent();
        NotGateComponent n1 = new NotGateComponent();
        NotGateComponent n2 = new NotGateComponent();
        NotGateComponent n3 = new NotGateComponent();
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

        String out = "output";
        String in = "input";

        // Construct chain
        p.connectToPort(w1, out);
        n1.connectToPort(w1, in);

        n1.connectToPort(w2, out);
        n2.connectToPort(w2, in);

        n2.connectToPort(w3, out);
        n3.connectToPort(w3, in);

        assertTrue(w1.isHigh());
        assertTrue(w2.isLow());
        assertTrue(w3.isHigh());

        w2.destroy();
        w3.destroy();

        Wire newW2 = new Wire();
        n1.connectToPort(newW2, out);
        n2.connectToPort(newW2, in);

        Wire newW3 = new Wire();
        n2.connectToPort(newW3, out);
        n3.connectToPort(newW3, in);

        assertTrue(w1.isHigh());
        assertTrue(w2.isUndefined());
        assertTrue(w3.isUndefined());
        assertTrue(newW2.isLow());
        assertTrue(newW3.isHigh());
    }
}
