package integration;

import static org.junit.jupiter.api.Assertions.*;

import org.cafebabe.model.components.NotGateComponent;
import org.cafebabe.model.components.OrGateComponent;
import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.components.connections.Wire;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class PowerChainTest {

    private static Wire on, off;

    @BeforeAll
    static void setup() {
        PowerSourceComponent power = new PowerSourceComponent();
        NotGateComponent not = new NotGateComponent();
        on = new Wire();
        off = new Wire();

        power.connectToPort(on, "output");
        not.connectToPort(on, "input");
        not.connectToPort(off, "output");
    }

    @Test
    void smallComponentChainShouldFlowPower() {
        AndGateComponent and = new AndGateComponent();
        Wire res = new Wire();

        // Setup AND-gate with both inputs active
        and.connectToPort(res, "output");
        assertTrue(res.isUndefined());

        and.connectToPort(on, "input1");
        assertFalse(res.isHigh());

        and.connectToPort(on, "input2");
        assertTrue(res.isHigh());

        // Connect OR-gate with the output of AND-gate
        OrGateComponent or = new OrGateComponent();
        Wire res2 = new Wire();

        or.connectToPort(res, "input1");
        or.connectToPort(off, "input2");
        or.connectToPort(res2, "output");

        assertTrue(res2.isHigh());
    }


    @Test
    void notGateChainShouldAlternate() {
        Wire prev = on;

        boolean flipper = false;
        for(int i = 0; i < 17; i++) {
            NotGateComponent or = new NotGateComponent();
            or.connectToPort(prev, "input");

            prev = new Wire();
            or.connectToPort(prev, "output");
            if(flipper) assertTrue(prev.isHigh());
            else assertTrue(prev.isLow());
            flipper = !flipper;
        }
    }

    @Test
    void reverseNotGateChainShouldAlternate() {
        PowerSourceComponent p = new PowerSourceComponent();

        Stack<Wire> wireStack = new Stack<>();
        Wire prev = new Wire();
        for(int i = 0; i < 17; i++) {
            NotGateComponent right = new NotGateComponent();
            right.connectToPort(prev, "output");

            wireStack.push(prev);
            assertTrue(prev.isUndefined());

            Wire w = new Wire();
            right.connectToPort(w, "input");
            prev = w;
        }

        wireStack.push(prev);
        assertTrue(prev.isUndefined());
        p.connectToPort(prev, "output");
        assertTrue(prev.isHigh());

        boolean flipper = true;
        int counter = 0;
        while(!wireStack.empty()) {
            if(flipper) assertTrue(wireStack.peek().isHigh());
            else assertTrue(wireStack.peek().isLow());
            wireStack.pop();
            flipper = !flipper;
        }

    }

    @Test
    void orGateChainShouldFlowPower() {
        Wire prev = on;

        for(int i = 0; i < 17; i++) {
            OrGateComponent or = new OrGateComponent();
            or.connectToPort(prev, "input1");
            or.connectToPort(off, "input2");

            prev = new Wire();
            or.connectToPort(prev, "output");
            assertTrue(prev.isHigh());
        }
    }

    @Test
    void reversedOrGateChainShouldFlowPower() {
        Wire res = new Wire();

        Wire prev = res;
        List<Wire> wires = new ArrayList<>();
        for(int i = 0; i < 17; i++) {
            OrGateComponent or = new OrGateComponent();
            or.connectToPort(prev, "output");
            or.connectToPort(off, "input2");
            assertTrue(prev.isUndefined());

            prev = new Wire();
            or.connectToPort(prev, "input1");

            wires.add(prev);
        }

        PowerSourceComponent p = new PowerSourceComponent();
        p.connectToPort(prev, "output");
        assertTrue(res.isHigh());

        for(Wire w : wires) {
            assertTrue(w.isHigh());
        }
    }
}
