package org.cafebabe.model.storage.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.storage.JsonStorage;

/**
 * A GSON TypeAdapter for Wire instances.
 */
public class WireAdapter extends TypeAdapter<Wire> {

    @Override
    public void write(JsonWriter writer, Wire value) throws IOException {
        writer.beginObject();
        writer.name("outputs");
        AdapterUtil.writeWirePortArray(writer, value.getConnectedOutputs());
        writer.name("inputs");
        AdapterUtil.writeWirePortArray(writer, value.getConnectedInputs());
        writer.endObject();
    }

    @Override
    public Wire read(JsonReader reader) throws IOException {
        reader.beginObject();

        Set<Long> connectedInputIds = new HashSet<>();
        Set<Long> connectedOutputIds = new HashSet<>();

        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "outputs":
                    connectedInputIds = AdapterUtil.readWirePortArray(reader);
                    break;
                case "inputs":
                    connectedOutputIds = AdapterUtil.readWirePortArray(reader);
                    break;
                default:
                    throw new RuntimeException(String.format(
                            "Could not recognize JSON name %s while reading wire", name)
                    );
            }
        }
        reader.endObject();

        Wire wire = new Wire();

        for (Long id : connectedInputIds) {
            connectWire(wire, id);
        }
        for (Long id : connectedOutputIds) {
            connectWire(wire, id);
        }

        return wire;
    }

    private void connectWire(Wire wire, Long portId) {
        Component target = JsonStorage.getPortIdComponentMap().get(portId);
        String portTag = JsonStorage.getComponentToIdTagMap().get(target).get(portId);
        target.connectToPort(wire, portTag);
    }
}
