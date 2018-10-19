package model.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.storage.adapters.ComponentAdapter;
import org.cafebabe.model.storage.adapters.StorageComponent;
import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class ComponentAdapterTest {
    private static Gson gson;

    private static final String AND_GATE_JSON =
            "{\"identifier\":\"GATE_And\",\"position\":[0,0],\"input\":{\"input1\":0,"
                    + "\"input2\":1},\"output\":{\"output\":2}}";
    private static final String MOVED_AND_GATE_JSON =
            "{\"identifier\":\"GATE_And\",\"position\":[10,20],\"input\":{\"input1\":0,"
                    + "\"input2\":1},\"output\":{\"output\":2}}";

    @BeforeAll
    static void setUp() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StorageComponent.class, new ComponentAdapter());
        gson = gsonBuilder.create();
    }

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.reset();
    }

    @Test
    void testNewAndGateWrite() {
        StorageComponent storageComponent = new StorageComponent(new AndGateComponent());

        assertEquals(AND_GATE_JSON, gson.toJson(storageComponent));
    }

    @Test
    void testMovedAndGateWrite() {
        AndGateComponent a = new AndGateComponent();
        a.getTrackablePosition().move(10, 20);
        StorageComponent storageComponent = new StorageComponent(a);

        assertEquals(MOVED_AND_GATE_JSON, gson.toJson(storageComponent));
    }

    @Test
    void testAndGateRead() {
        var wrapper = new Object() {
            StorageComponent component;
        };
        assertDoesNotThrow(() -> wrapper.component = gson.fromJson(AND_GATE_JSON,
                StorageComponent.class));
        Component component = wrapper.component.create();
        assertEquals(0, component.getTrackablePosition().getX());
        assertEquals(0, component.getTrackablePosition().getY());
    }

    @Test
    void testMovedAndGateRead() {
        var wrapper = new Object() {
            StorageComponent component;
        };
        assertDoesNotThrow(() -> wrapper.component = gson.fromJson(MOVED_AND_GATE_JSON,
                StorageComponent.class));
        Component component = wrapper.component.create();
        assertEquals(10, component.getTrackablePosition().getX());
        assertEquals(20, component.getTrackablePosition().getY());
    }

    @Test
    void noDisplayNameShouldThrow() {
        String noDisplayName = AND_GATE_JSON.replace("GATE_And", "");
        assertThrows(RuntimeException.class, () -> gson.fromJson(noDisplayName,
                StorageComponent.class));
    }

    @Test
    void brokenPositionShouldThrow() {
        String noPosition = AND_GATE_JSON.replace("[0,0]", "");
        assertThrows(RuntimeException.class, () -> gson.fromJson(noPosition,
                StorageComponent.class));

        String brokenPosition = AND_GATE_JSON.replace("[0,0]", "[0]");
        assertThrows(RuntimeException.class, () -> gson.fromJson(brokenPosition,
                StorageComponent.class));

        String brokenPosition2 = AND_GATE_JSON.replace("[0,0]", "[0,0,0]");
        assertThrows(RuntimeException.class, () -> gson.fromJson(brokenPosition2,
                StorageComponent.class));
    }

    @Test
    void brokenPortIdsShouldThrow() {
        String noPorts = AND_GATE_JSON.replace("{\"input1\":0,\"input2\":1}", "")
                .replace("{\"output\":2}", "");
        assertThrows(RuntimeException.class, () -> gson.fromJson(noPorts,
                StorageComponent.class));
    }

    @Test
    void invalidJsonNameShouldThrow() {
        String extraTag = "{\"identifier\":\"AND Gate\",\"position\":[0,0],"
                + "\"input\":{\"input1\":0,\"input2\":1},\"output\":{\"output\":2},"
                + "\"test\":[]}"; // Note this extra piece here.

        assertThrows(RuntimeException.class, () -> gson.fromJson(extraTag,
                StorageComponent.class));
    }
}