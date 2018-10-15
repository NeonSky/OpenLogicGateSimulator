package model.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.gate.NotGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.PowerSourceComponent;
import org.cafebabe.model.storage.JsonStorage;
import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.TooManyMethods")
class JsonStorageTest {

    private static final Path TEST_PATH = Paths.get("test/");

    private static final String EMPTY_CIRCUIT = "{\"components\":[],\"connections\":[]}";
    private static final String FILLED_CIRCUIT = "{\"components\":[{\"identifier\":"
            + "\"NOT_Gate\",\"position\":[0,0],\"input\":{\"input\":1},\"output\":{"
            + "\"output\":2}},{\"identifier\":\"POWER_Source\",\"position\":[0,0],"
            + "\"input\":{},\"output\":{\"output\":0}}],\"connections\":[{\"outputs\":[0],"
            + "\"inputs\":[1]}]}";
    @BeforeAll
    static void setUp() {
        // Create test file directory
        try {
            Files.createDirectory(TEST_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create test reading file
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("test/test_read.json"),
                    StandardCharsets.UTF_8);
            writer.write(FILLED_CIRCUIT);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        // Remove test files and directory
        try {
            Files.walk(TEST_PATH).map(Path::toFile).forEach(File::delete);
            Files.delete(TEST_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.setStartingValue(0);
    }

    @Test
    void testSaveEmptyWorkspace() {
        Workspace workspace = new Workspace();

        Writer stringWriter = new StringWriter();
        JsonStorage jsonStorage = new JsonStorage();
        jsonStorage.setWriter(stringWriter);
        jsonStorage.saveWorkspace(workspace, null);

        assertEquals(EMPTY_CIRCUIT, stringWriter.toString());
    }

    @Test
    void testSaveWorkspaceWithComponentAndWires() {
        Workspace workspace = new Workspace();
        JsonStorage jsonStorage = new JsonStorage();
        Writer stringWriter = new StringWriter();
        jsonStorage.setWriter(stringWriter);

        PowerSourceComponent powerSource = new PowerSourceComponent();
        NotGateComponent notGateComponent = new NotGateComponent();
        Wire wire = new Wire();

        workspace.getCircuit().addComponent(notGateComponent);
        workspace.getCircuit().addComponent(powerSource);
        workspace.getCircuit().addWire(wire);

        powerSource.connectToPort(wire, "output");
        notGateComponent.connectToPort(wire, "input");

        jsonStorage.saveWorkspace(workspace, null);
        assertEquals(FILLED_CIRCUIT, stringWriter.toString());
    }

    @Test
    void testReadEmptyWorkspace() {
        JsonStorage jsonStorage = new JsonStorage();
        Reader stringReader = new StringReader(EMPTY_CIRCUIT);
        jsonStorage.setReader(stringReader);

        var wrapper = new Object() {
            Workspace workspace;
        };

        assertDoesNotThrow(() -> wrapper.workspace = jsonStorage.loadWorkspace(""));
        assertTrue(wrapper.workspace.getCircuit().getWires().isEmpty());
        assertTrue(wrapper.workspace.getCircuit().getComponents().isEmpty());
    }

    @Test
    void testReadFilledWorkspace() {
        JsonStorage jsonStorage = new JsonStorage();
        Reader stringReader = new StringReader(FILLED_CIRCUIT);
        jsonStorage.setReader(stringReader);

        var wrapper = new Object() {
            Workspace workspace;
        };

        assertDoesNotThrow(() -> wrapper.workspace = jsonStorage.loadWorkspace(""));
        assertEquals(1, wrapper.workspace.getCircuit().getWires().size());
        assertEquals(2, wrapper.workspace.getCircuit().getComponents().size());
    }

    @Test
    void readingInvalidComponentsTagShouldThrow() {
        JsonStorage jsonStorage = new JsonStorage();
        Reader stringReader = new StringReader("{\"invalid_name\"}");
        jsonStorage.setReader(stringReader);

        assertThrows(RuntimeException.class, () -> jsonStorage.loadWorkspace(""));
    }

    @Test
    void readingInvalidConnectionsTagShouldThrow() {
        JsonStorage jsonStorage = new JsonStorage();
        Reader stringReader = new StringReader("{\"components\":[],\"invalid_name\"}");
        jsonStorage.setReader(stringReader);

        assertThrows(RuntimeException.class, () -> jsonStorage.loadWorkspace(""));
    }

    @Test
    void readingNonExistentWorkspaceShouldThrow() {
        JsonStorage jsonStorage = new JsonStorage();
        assertThrows(RuntimeException.class, () -> jsonStorage.loadWorkspace(""));
    }

    @Test
    void testWritingToFile() {
        Workspace workspace = new Workspace();
        Circuit circuit = workspace.getCircuit();

        PowerSourceComponent powerSource = new PowerSourceComponent();
        NotGateComponent notGateComponent = new NotGateComponent();
        Wire wire = new Wire();

        circuit.addComponent(notGateComponent);
        circuit.addComponent(powerSource);
        circuit.addWire(wire);

        powerSource.connectToPort(wire, "output");
        notGateComponent.connectToPort(wire, "input");

        JsonStorage jsonStorage = new JsonStorage();
        jsonStorage.saveWorkspace(workspace, "test/test_write.json");
        assertTrue(new File("test/test_write.json").isFile());
    }

    @Test
    void testReadingFromFile() {
        JsonStorage jsonStorage = new JsonStorage();

        var wrapper = new Object() {
            Workspace workspace;
        };

        assertDoesNotThrow(() -> wrapper.workspace = jsonStorage.loadWorkspace(
                "test/test_read.json"));

        assertEquals(1, wrapper.workspace.getCircuit().getWires().size());
        assertEquals(2, wrapper.workspace.getCircuit().getComponents().size());
    }

    @Test
    void testSavingToInvalidFileShouldThrow() {
        JsonStorage jsonStorage = new JsonStorage();
        Workspace workspace = new Workspace();

        assertThrows(RuntimeException.class, () -> jsonStorage.saveWorkspace(workspace, ""));
    }
}
