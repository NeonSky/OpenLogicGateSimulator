package integration;

import static org.junit.jupiter.api.Assertions.*;

import org.cafebabe.model.components.OrGateComponent;
import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.Test;

public class PowerChainTest {

    @Test
    void ShouldChainThroughOrGate() {
        PowerSourceComponent p = new PowerSourceComponent();
        OrGateComponent or = new OrGateComponent();
        Wire pOut = new Wire();
        Wire orOut = new Wire();

        p.connectToPort(pOut, "output");
        or.connectToPort(pOut, "input1");
        or.connectToPort(orOut, "output");

        assertTrue(orOut.isActive());
    }

    @Test
    void ShouldChainThroughAndGate() {
        PowerSourceComponent p = new PowerSourceComponent();
        PowerSourceComponent p2 = new PowerSourceComponent();
        AndGateComponent and = new AndGateComponent();
        Wire pOut = new Wire();
        Wire p2Out = new Wire();
        Wire andOut = new Wire();

        p.connectToPort(pOut, "output");
        p2.connectToPort(p2Out, "output");
        and.connectToPort(andOut, "output");
        assertFalse(andOut.isActive());

        and.connectToPort(pOut, "input1");
        assertFalse(andOut.isActive());

        and.connectToPort(p2Out, "input2");
        assertTrue(andOut.isActive());
    }
}
