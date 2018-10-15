package model.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.BiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.storage.JsonStorage;
import org.cafebabe.model.storage.adapters.ComponentAdapter;
import org.cafebabe.model.storage.adapters.StorageComponent;
import org.cafebabe.model.storage.adapters.WireAdapter;
import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class WireAdapterTest {
    private static Gson gson;

    private static final String NEW_WIRE_JSON = "{\"outputs\":[],\"inputs\":[]}";
    private static final String CONNECTED_WIRE_JSON = "{\"outputs\":[2],\"inputs\":[0]}";

    @BeforeAll
    static void setUp() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(StorageComponent.class, new ComponentAdapter());
        gsonBuilder.registerTypeAdapter(Wire.class, new WireAdapter());

        gson = gsonBuilder.create();
    }

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.setStartingValue(0);
    }

    @Test
    void testNewWireWrite() {
        Wire w = new Wire();

        assertEquals(NEW_WIRE_JSON, gson.toJson(w));
    }

    @Test
    void testConnectedWireWrite() {
        Wire w = new Wire();
        Component and = new AndGateComponent();
        and.connectToPort(w, "input1");
        and.connectToPort(w, "output");

        assertEquals(CONNECTED_WIRE_JSON, gson.toJson(w));
    }

    @Test
    void testWireRead() {
        var wrapper = new Object() {
            Wire wire;
        };
        assertDoesNotThrow(() -> wrapper.wire = gson.fromJson(NEW_WIRE_JSON, Wire.class));
        assertTrue(wrapper.wire.isUndefined());
        assertFalse(wrapper.wire.isAnyInputConnected());
        assertFalse(wrapper.wire.isAnyOutputConnected());
    }

    @Test
    void testConnectedWireRead() {
        Wire w = new Wire();
        Component and = new AndGateComponent();
        StorageComponent storageComponent = new StorageComponent(and);

        and.connectToPort(w, "input1");
        and.connectToPort(w, "output");

        BiMap<Long, String> allPorts = storageComponent.getAllPorts().inverse();
        for (long i : allPorts.keySet()) {
            JsonStorage.getPortIdComponentMap().put(i, storageComponent.create());
        }
        JsonStorage.getComponentToIdTagMap().put(storageComponent.create(), allPorts);

        var wrapper = new Object() {
            Wire wire;
        };
        assertDoesNotThrow(() -> wrapper.wire = gson.fromJson(CONNECTED_WIRE_JSON, Wire.class));
        assertTrue(wrapper.wire.isUndefined());
        assertTrue(wrapper.wire.isAnyInputConnected());
        assertTrue(wrapper.wire.isAnyOutputConnected());
    }
}
