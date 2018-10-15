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

    public static void writeComponentPorts(JsonWriter writer, BiMap<String, Long> ports)
            throws IOException {
        writer.beginObject();
        for (Map.Entry<String, Long> entry : ports.entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
        }
        writer.endObject();
    }

    public static BiMap<String, Long> readComponentPorts(JsonReader reader) throws IOException {
        BiMap<String, Long> tagToPortIdMap = HashBiMap.create();

        reader.beginObject();
        while (reader.hasNext()) {
            String tag = reader.nextName();
            long id = reader.nextLong();
            tagToPortIdMap.put(tag, id);
        }
        reader.endObject();

        return tagToPortIdMap;
    }

    public static void writeWirePortArray(JsonWriter writer, Set<? extends Port> ports)
            throws IOException {
        writer.beginArray();

        List<Long> portIds = new ArrayList<>();
        for (Port p : ports) {
            portIds.add(p.getId());
        }
        Collections.sort(portIds);

        for (Long id : portIds) {
            writer.value(id);
        }
        writer.endArray();
    }

    public static Set<Long> readWirePortArray(JsonReader reader) throws IOException {
        Set<Long> ids = new HashSet<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ids.add(reader.nextLong());
        }
        reader.endArray();

        return ids;
    }
}
