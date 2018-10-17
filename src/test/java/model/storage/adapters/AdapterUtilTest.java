package model.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.BiMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.storage.adapters.AdapterUtil;
import org.cafebabe.model.storage.adapters.StorageComponent;
import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
class AdapterUtilTest {

    private static final String ORIGIN_POSITION_ARRAY = "[0,0]";
    private static final String MOVED_POSITION_ARRAY = "[10,-20]";
    private static final String AND_GATE_PORTS = "{\"input1\":0,\"input2\":1,\"output\":2}";
    private static final String EMPTY_PORT_ARRAY = "[]";
    private static final String SINGLE_OUT_PORT_ARRAY = "[2]";
    private static final String SINGLE_IN_PORT_ARRAY = "[1]";

    private static JsonWriter createJsonWriter(StringWriter writer) {
        return new JsonWriter(writer);
    }

    private static JsonReader createJsonReader(String input) {
        StringReader reader = new StringReader(input);
        return new JsonReader(reader);
    }

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.reset();
    }

    @Test
    void testWriteOriginPositionArray() {
        StringWriter writer = new StringWriter();

        Position pos = new Position(0,0);
        assertDoesNotThrow(() -> AdapterUtil.writePositionArray(createJsonWriter(writer), pos));
        assertEquals(ORIGIN_POSITION_ARRAY, writer.toString());
    }

    @Test
    void testReadOriginPositionArray() {
        JsonReader reader = createJsonReader(ORIGIN_POSITION_ARRAY);

        var wrapper = new Object() {
            Position pos;
        };
        assertDoesNotThrow(() -> wrapper.pos = AdapterUtil.readPositionArray(reader));
        assertEquals(0, wrapper.pos.getX());
        assertEquals(0, wrapper.pos.getY());
    }

    @Test
    void testWriteMovedPositionArray() {
        StringWriter writer = new StringWriter();

        Position pos = new Position(10,-20);
        assertDoesNotThrow(() -> AdapterUtil.writePositionArray(createJsonWriter(writer), pos));
        assertEquals(MOVED_POSITION_ARRAY, writer.toString());
    }

    @Test
    void testReadMovedPositionArray() {
        JsonReader reader = createJsonReader(MOVED_POSITION_ARRAY);

        var wrapper = new Object() {
            Position pos;
        };
        assertDoesNotThrow(() -> wrapper.pos = AdapterUtil.readPositionArray(reader));
        assertEquals(10, wrapper.pos.getX());
        assertEquals(-20, wrapper.pos.getY());
    }

    @Test
    void testWriteComponentPorts() {
        StringWriter writer = new StringWriter();

        StorageComponent component = new StorageComponent(new AndGateComponent());
        assertDoesNotThrow(() -> AdapterUtil.writeComponentPorts(
                createJsonWriter(writer), component.getAllPorts()));
        assertEquals(AND_GATE_PORTS, writer.toString());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testReadComponentPorts() {
        JsonReader reader = createJsonReader(AND_GATE_PORTS);

        var wrapper = new Object() {
            BiMap<String, Long> ports;
        };
        assertDoesNotThrow(() -> wrapper.ports = AdapterUtil.readComponentPorts(reader));
        assertEquals(3, wrapper.ports.size());
        assertTrue(wrapper.ports.containsKey("input1"));
        assertTrue(wrapper.ports.containsKey("input2"));
        assertTrue(wrapper.ports.containsKey("output"));
        assertTrue(wrapper.ports.containsValue(0L));
        assertTrue(wrapper.ports.containsValue(1L));
        assertTrue(wrapper.ports.containsValue(2L));
    }

    @Test
    void testWriteEmptyWireInPortArray() {
        StringWriter writer = new StringWriter();

        Wire wire = new Wire();
        assertDoesNotThrow(() -> AdapterUtil.writeWirePortArray(
                createJsonWriter(writer), wire.getConnectedInputs()));
        assertEquals(EMPTY_PORT_ARRAY, writer.toString());
    }

    @Test
    void testWriteEmptyWireOutPortArray() {
        StringWriter writer = new StringWriter();

        Wire wire = new Wire();
        assertDoesNotThrow(() -> AdapterUtil.writeWirePortArray(
                createJsonWriter(writer), wire.getConnectedOutputs()));
        assertEquals(EMPTY_PORT_ARRAY, writer.toString());
    }

    @Test
    void testReadEmptyWirePortArray() {
        JsonReader reader = createJsonReader(EMPTY_PORT_ARRAY);

        var wrapper = new Object() {
            Set<Long> ports;
        };
        assertDoesNotThrow(() -> wrapper.ports =
                AdapterUtil.readWirePortArray(reader));
        assertIterableEquals(new HashSet<Integer>(), wrapper.ports);
    }

    @Test
    void testWriteWireOutPortArray() {
        StringWriter writer = new StringWriter();

        Wire wire = new Wire();
        Component and = new AndGateComponent();
        and.connectToPort(wire, "output");
        and.connectToPort(wire, "input1");

        assertDoesNotThrow(() -> AdapterUtil.writeWirePortArray(
                createJsonWriter(writer), wire.getConnectedOutputs()));
        assertEquals(SINGLE_OUT_PORT_ARRAY, writer.toString());
    }

    @Test
    void testWriteWireInPortArray() {
        StringWriter writer = new StringWriter();

        Wire wire = new Wire();
        Component and = new AndGateComponent();
        and.connectToPort(wire, "output");
        and.connectToPort(wire, "input2");

        assertDoesNotThrow(() -> AdapterUtil.writeWirePortArray(
                createJsonWriter(writer), wire.getConnectedInputs()));
        assertEquals(SINGLE_IN_PORT_ARRAY, writer.toString());
    }

}
