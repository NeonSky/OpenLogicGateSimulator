package org.cafebabe.model.storage.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A GSON type adapter for serializing/deserializing StorageComponents.
 * This class defines how GSON should handle any StorageComponent objects
 * it encounters while storing or loading a workspace.
 */
public class ComponentAdapter extends TypeAdapter<StorageComponent> {


    @Override
    public void write(JsonWriter writer, StorageComponent component) throws IOException {
        writer.beginObject();
        writer.name("display_name").value(component.getDisplayName());
        writer.name("position");
        AdapterUtil.writePositionArray(writer, component.getPosition());
        writer.name("input");
        AdapterUtil.writeComponentPorts(writer, component.getInputTagsToIdsMap());
        writer.name("output");
        AdapterUtil.writeComponentPorts(writer, component.getOutputTagsToIdsMap());
        writer.endObject();
    }

    @Override
    public StorageComponent read(JsonReader reader) throws IOException {
        ComponentData data = readComponentData(reader);

        if (data.isValid()) {
            return new StorageComponent(data);
        } else {
            throw new IOException("Could not read valid component data");
        }
    }

    private ComponentData readComponentData(JsonReader reader) throws IOException {
        ComponentData data = new ComponentData();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "display_name":
                    data.setDisplayName(reader.nextString());
                    break;
                case "position":
                    data.setPosition(AdapterUtil.readPositionArray(reader));
                    break;
                case "input":
                    data.setInputIds(AdapterUtil.readComponentPorts(reader));
                    break;
                case "output":
                    data.setOutputIds(AdapterUtil.readComponentPorts(reader));
                    break;
                default:
                    throw new RuntimeException(String.format(
                            "Could not recognize JSON name %s while reading component", name)
                    );
            }
        }
        reader.endObject();

        return data;
    }
}