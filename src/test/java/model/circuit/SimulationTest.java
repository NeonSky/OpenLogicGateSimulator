package model.circuit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicStateContainer;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.gate.XorGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.PowerSourceComponent;
import org.junit.jupiter.api.Test;

public class SimulationTest {

    static int dfsHelperCalls;

    // Runs two (top/bot) parallell lines of power that will race towards a single XOR.
    @Test
    void xorShouldLeakPowerWithDfs() {
        // on = HIGH value
        OutputPort powerSource = new OutputPort();
        powerSource.setState(LogicState.HIGH);

        // Left part
        Wire topLeft = new Wire();
        Wire botLeft = new Wire();
        topLeft.connectOutputPort(powerSource);
        botLeft.connectOutputPort(powerSource);

        // Mid part
        NotGateComponent topNot = new NotGateComponent();
        NotGateComponent botNot = new NotGateComponent();
        topNot.connectToPort(topLeft, "input");
        botNot.connectToPort(botLeft, "input");

        // Right part
        Wire topRight = new Wire();
        Wire botRight = new Wire();
        topNot.connectToPort(topRight, "output");
        botNot.connectToPort(botRight, "output");

        assertTrue(topRight.isLow());
        assertTrue(botRight.isLow());

        // XOR
        XorGateComponent xor = new XorGateComponent();
        xor.connectToPort(topRight, "input1");
        xor.connectToPort(botRight, "input2");

        // Result wire
        Wire res = new Wire();
        xor.connectToPort(res, "output");

        res.onStateChangedEvent().addListener(this::xorShouldLeakPowerWithDfsHelper);

        assertTrue(res.isLow());
        powerSource.setState(LogicState.LOW);
        assertTrue(res.isLow());
    }

    void xorShouldLeakPowerWithDfsHelper(LogicStateContainer wire) {
        // The state should bleed to HIGH before reaching LOW
        if (dfsHelperCalls == 0) {
            assertTrue(wire.isHigh());
        } else {
            assertTrue(wire.isLow());
        }
        dfsHelperCalls++;
    }

    @Test
    void xorShouldNotLeakPowerWithBfs() {
        Circuit c = new Circuit(); // Circuit uses Simulator which uses a BFS approach
        c.toggleSimulationState();

        Wire on = new Wire();
        c.addWire(on);

        XorGateComponent xor = new XorGateComponent();
        c.addComponent(xor);
        Wire res = new Wire();
        c.addWire(res);
        xor.connectToPort(res, "output");

        // The state should ONLY be updated to LOW and never to e.g. HIGH
        res.onStateChangedEvent().addListener((wire) -> assertTrue(wire.isLow()));

        xor.connectToPort(on, "input1");
        xor.connectToPort(on, "input2");

        assertTrue(res.isUndefined());
        PowerSourceComponent power = new PowerSourceComponent();
        c.addComponent(power);
        assertTrue(res.isUndefined());
        power.connectToPort(on, "output");

        // Ensure that the state is actually changed
        try {
            Thread.sleep(10); // arbitrary wait time for the simulator
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(res.isLow());
    }
}
