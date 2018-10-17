package model.components.connections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.editor.workspace.circuit.component.position.TrackablePosition;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortAlreadyAddedException;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortNotConnectedException;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
class WireTest {
    @Test
    void newWireShouldNotBeConnected() {
        Wire w = new Wire();

        assertFalse(w.isAnyInputConnected());
        assertFalse(w.isAnyOutputConnected());
    }

    @Test
    void testConnectingSingleInputPort() {
        Wire w = new Wire();
        InputPort in = new InputPort();

        w.connectInputPort(in);
        assertTrue(w.isAnyInputConnected());
    }

    @Test
    void connectingSameInputPortSeveralTimesShouldThrow() {
        Wire w = new Wire();
        InputPort in = new InputPort();

        w.connectInputPort(in);

        assertThrows(RuntimeException.class, () -> w.connectInputPort(in));
    }

    @Test
    void connectingInputPortShouldSetPosition() {
        Wire w = new Wire();
        InputPort in = new InputPort();
        IReadOnlyMovable positionTracker = new TrackablePosition(new Position(10, 20));
        in.setPositionTracker(positionTracker);

        w.connectInputPort(in);

        assertEquals(10, w.getEndPos().getX());
        assertEquals(20, w.getEndPos().getY());
    }

    @Test
    void testConnectingSingleOutputPort() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        w.connectOutputPort(out);
        assertTrue(w.isAnyOutputConnected());
    }

    @Test
    void addingSameOutputPortSeveralTimesShouldThrow() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        w.connectOutputPort(out);
        assertThrows(PortAlreadyAddedException.class, () -> w.connectOutputPort(out));
    }

    @Test
    void connectingOutputPortShouldSetPosition() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();
        IReadOnlyMovable positionTracker = new TrackablePosition(new Position(10, 20));
        out.setPositionTracker(positionTracker);

        w.connectOutputPort(out);

        assertEquals(10, w.getStartPos().getX());
        assertEquals(20, w.getStartPos().getY());
    }

    @Test
    void testDisconnectingInputPort() {
        Wire w = new Wire();
        InputPort in = new InputPort();

        w.connectInputPort(in);

        assertDoesNotThrow(() -> w.disconnectInputPort(in));
        assertFalse(w.isAnyInputConnected());
    }

    @Test
    void disconnectingNotConnectedInputPortShouldThrow() {
        Wire w = new Wire();
        InputPort in = new InputPort();

        assertThrows(PortNotConnectedException.class, () -> w.disconnectInputPort(in));
    }

    @Test
    void testDisconnectingOutputPort() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        w.connectOutputPort(out);

        assertDoesNotThrow(() -> w.disconnectOutputPort(out));
    }

    @Test
    void disconnectingNotConnectedOutputPortShouldThrow() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        assertThrows(PortNotConnectedException.class, () -> w.disconnectOutputPort(out));
    }

    @Test
    void testDisconnectAll() {
        Wire w = new Wire();
        w.connectInputPort(new InputPort());
        w.connectInputPort(new InputPort());
        w.connectInputPort(new InputPort());
        w.connectInputPort(new InputPort());
        w.connectOutputPort(new OutputPort());
        w.connectOutputPort(new OutputPort());
        w.connectOutputPort(new OutputPort());
        w.connectOutputPort(new OutputPort());

        assertTrue(w.isAnyInputConnected());
        assertTrue(w.isAnyOutputConnected());

        w.disconnectAll();
        assertFalse(w.isAnyInputConnected());
        assertFalse(w.isAnyOutputConnected());
    }

    @Test
    void wireLogicStateShouldDefaultToUndefined() {
        Wire w = new Wire();
        assertEquals(LogicState.UNDEFINED, w.logicState());
    }

    @Test
    void wireLogicStateShouldMatchConnectedOutput() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        w.connectOutputPort(out);

        out.setState(LogicState.HIGH);
        assertEquals(LogicState.HIGH, w.logicState());

        out.setState(LogicState.LOW);
        assertEquals(LogicState.LOW, w.logicState());
    }

    @Test
    void disconnectingOutputPortShouldSetWireLogicStateToUndefined() {
        Wire w = new Wire();
        OutputPort out = new OutputPort();

        w.connectOutputPort(out);
        out.setState(LogicState.HIGH);
        assertEquals(LogicState.HIGH, w.logicState());

        w.disconnectOutputPort(out);

        assertEquals(LogicState.UNDEFINED, w.logicState());
    }

}
