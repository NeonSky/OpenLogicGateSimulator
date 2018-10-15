package org.cafebabe.model.storage.adapters;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;

/**
 * A number of helper methods for the custom GSON type adapters used to
 * save and load workspace objects.
 * Methods defined here include helpers from reading/writing positions,
 * port ID arrays, and wire connection arrays.
 */
public final class AdapterUtil {

    private AdapterUtil() {}

    /* Package private */
    public static void writePositionArray(JsonWriter writer, Position position)
            throws IOException {
        writer.beginArray();
        writer.value(position.getX());
        writer.value(position.getY());
        writer.endArray();
    }

    public static Position readPositionArray(JsonReader reader) throws IOException {
        reader.beginArray();
        Position pos = new Position(reader.nextInt(), reader.nextInt());
        reader.endArray();

        return pos;
    }

    public static void writeComponentPorts(JsonWriter writer, BiMap<String, Integer> ports)
            throws IOException {
        writer.beginObject();
        for (Map.Entry<String, Integer> entry : ports.entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
        }
        writer.endObject();
    }

    public static BiMap<String, Integer> readComponentPorts(JsonReader reader) throws IOException {
        BiMap<String, Integer> tagToPortIdMap = HashBiMap.create();

        reader.beginObject();
        while (reader.hasNext()) {
            String tag = reader.nextName();
            int id = reader.nextInt();
            tagToPortIdMap.put(tag, id);
        }
        reader.endObject();

        return tagToPortIdMap;
    }

    public static void writeWirePortArray(JsonWriter writer, Set<? extends Port> ports)
            throws IOException {
        writer.beginArray();

        List<Integer> portIds = new ArrayList<>();
        for (Port p : ports) {
            portIds.add(p.getId());
        }
        Collections.sort(portIds);

        for (Integer id : portIds) {
            writer.value(id);
        }
        writer.endArray();
    }

    public static Set<Integer> readWirePortArray(JsonReader reader) throws IOException {
        Set<Integer> ids = new HashSet<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ids.add(reader.nextInt());
        }
        reader.endArray();

        return ids;
    }
}
