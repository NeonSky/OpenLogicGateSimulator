package org.cafebabe.model.storage;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.Setter;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.storage.adapters.ComponentAdapter;
import org.cafebabe.model.storage.adapters.StorageComponent;
import org.cafebabe.model.storage.adapters.WireAdapter;

/**
 * The main class for saving/loading workspaces as JSON structures using the
 * GSON library.
 * Exposes methods for saving and loading a workspace via the ISaveLoadWorkspaces
 * interface.
 */
@SuppressWarnings("PMD.DataClass")
public class JsonStorage implements ISaveLoadWorkspaces {
    /*
     * These maps are the central part for reconstructing circuits.
     *
     * The first one maps each port id found in a save file to the corresponding component.
     * The second maps from a component to a bidirectional map, which maps
     * port tags <-> port ids.
     */
    private static final Map<Long, Component> PORT_ID_COMPONENT_MAP = new HashMap<>();
    private static final Map<Component, BiMap<Long, String>>
            COMPONENT_TO_ID_TAG_MAP = new HashMap<>();

    private final Gson gson;
    @Setter private Writer writer;
    @Setter private Reader reader;

    public JsonStorage() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StorageComponent.class, new ComponentAdapter());
        gsonBuilder.registerTypeAdapter(Wire.class, new WireAdapter());
        this.gson = gsonBuilder.create();
    }

    public static Map<Long, Component> getPortIdComponentMap() {
        return PORT_ID_COMPONENT_MAP;
    }

    public static Map<Component, BiMap<Long, String>> getComponentToIdTagMap() {
        return COMPONENT_TO_ID_TAG_MAP;
    }

    @Override
    public Workspace loadWorkspace(String location) {
        if (Objects.isNull(this.reader) || this.reader instanceof BufferedReader) {
            try {
                this.reader = Files.newBufferedReader(Paths.get(location), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JsonReader reader = new JsonReader(this.reader);

        Workspace workspace = new Workspace();

        try {
            readWorkspace(workspace, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workspace.setPath(location);
        PORT_ID_COMPONENT_MAP.clear();
        COMPONENT_TO_ID_TAG_MAP.clear();

        return workspace;
    }

    @Override
    public void saveWorkspace(Workspace workspace, String location) {
        if (Objects.isNull(this.writer) || this.writer instanceof BufferedWriter) {
            try {
                String path = validateFilename(location);
                workspace.setPath(path);
                this.writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        JsonWriter jsonWriter = new JsonWriter(this.writer);

        try {
            writeCircuit(workspace.getCircuit(), jsonWriter);
            jsonWriter.flush();
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PORT_ID_COMPONENT_MAP.clear();
        COMPONENT_TO_ID_TAG_MAP.clear();
    }

    private String validateFilename(String location) {
        if (Strings.isNullOrEmpty(location)) {
            throw new RuntimeException("Invalid save path specified");
        }

        // Append olgs file extension if not present
        String path = location;
        if (!location.endsWith(".olgs")) {
            StringBuilder pathBuilder = new StringBuilder(location);
            pathBuilder.append(".olgs");
            path = pathBuilder.toString();
        }

        return path;
    }

    private void writeCircuit(Circuit circuit, JsonWriter writer) throws IOException {
        writer.beginObject(); // begin whole circuit
        writer.name("components");
        writer.beginArray(); // begin writing "components" section
        writeComponents(circuit.getComponents(), writer);
        writer.endArray(); // end "components" section
        writer.name("connections");
        writer.beginArray(); // begin writing "connections" section
        writeWires(circuit.getWires(), writer);
        writer.endArray(); // end "connections" section
        writer.endObject(); // end whole circuit
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void writeComponents(Set<Component> components, JsonWriter writer) throws IOException {
        for (Component component : components) {
            StorageComponent storageComponent = new StorageComponent(component);
            String json = this.gson.toJson(storageComponent);
            writer.jsonValue(json);
        }
    }

    private void writeWires(Set<Wire> wires, JsonWriter writer) throws IOException {
        for (Wire wire : wires) {
            String json = this.gson.toJson(wire);
            writer.jsonValue(json);
        }
    }

    private void readWorkspace(Workspace workspace, JsonReader reader) throws IOException {
        reader.beginObject(); // begin reading circuit
        if (!reader.nextName().equals("components")) {
            throw new IOException("Invalid save file format");
        }
        reader.beginArray(); // begin reading components
        readComponents(reader, workspace.getCircuit());
        reader.endArray(); // end reading components
        if (!reader.nextName().equals("connections")) {
            throw new IOException("Invalid save file format");
        }
        reader.beginArray(); // begin reading connections
        readWires(reader, workspace.getCircuit());
        reader.endArray(); // end reading connections
        reader.endObject(); // end reading circuit
    }

    private void readComponents(JsonReader reader, Circuit circuit) throws IOException {
        while (reader.hasNext()) {
            StorageComponent component = this.gson.fromJson(reader, StorageComponent.class);
            circuit.addComponent(component.create());

            BiMap<Long, String> allPorts = component.getAllPorts().inverse();
            // Map all this components' ports to this component
            for (long i : allPorts.keySet()) {
                PORT_ID_COMPONENT_MAP.put(i, component.create());
            }
            // Map this component to its bidirectional map of port tag <-> port id
            COMPONENT_TO_ID_TAG_MAP.put(component.create(), allPorts);
        }
    }

    private void readWires(JsonReader reader, Circuit circuit) throws IOException {
        while (reader.hasNext()) {
            Wire wire = this.gson.fromJson(reader, Wire.class);
            circuit.addWire(wire);
        }
    }
}
