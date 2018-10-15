package org.cafebabe.model.storage.adapters;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import org.cafebabe.model.editor.util.ComponentUtil;
import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;


/**
 * A helper class for storing/loading components. This solves 2 major
 * problems.
 * 1. When storing a component with GSON, we call gson.toJson(obj).
 * To be able to specify how GSON should store this component, we use
 * TypeAdapters. Registering a type adapter for every type of component
 * that exists in the model is both tedious and impractical.
 * 2. When reading a component with GSON, we call
 * gson.fromJson(JsonReader, Type).
 * The Type parameter specifies the type to which the instantiated object
 * should be cast before returning. When loading components, it is impossible
 * to know and specify the type in advance. To work around this, we always save
 * and load StorageComponent objects, which can be converted to the real model
 * object using the create() method in this class.
 */

public class StorageComponent {

    @Getter private final String identifier;
    @Getter private final Position position;
    @Getter private final BiMap<String, Integer> inputTagsToIdsMap;
    @Getter private final BiMap<String, Integer> outputTagsToIdsMap;
    private Component component;

    public StorageComponent(Component component) {
        this.identifier = component.getIdentifier();

        this.position = new Position(component.getTrackablePosition().getX(),
                component.getTrackablePosition().getY());

        this.inputTagsToIdsMap = HashBiMap.create();
        Map<String, InputPort> tagToInput = component.getTagToInput();
        List<String> tags = new ArrayList<>(tagToInput.keySet());
        Collections.sort(tags);
        for (String tag : tags) {
            this.inputTagsToIdsMap.put(tag, tagToInput.get(tag).getId());
        }

        this.outputTagsToIdsMap = HashBiMap.create();
        Map<String, OutputPort> tagToOutput = component.getTagToOutput();
        tags = new ArrayList<>(tagToOutput.keySet());
        Collections.sort(tags);
        for (String tag : tags) {
            this.outputTagsToIdsMap.put(tag, tagToOutput.get(tag).getId());
        }
    }

    StorageComponent(ComponentData data) {
        this.identifier = data.getIdentifier();
        this.position = data.getPosition();
        this.inputTagsToIdsMap = data.getInputIds();
        this.outputTagsToIdsMap = data.getOutputIds();
    }

    /* Public */
    public Component create() {
        if (Objects.isNull(this.component)) {
            this.component = ComponentUtil.componentFactory(this.identifier);

            if (Objects.isNull(this.component)) {
                throw new RuntimeException("Could not create component with display name "
                        + this.identifier);
            }

            this.component.getTrackablePosition().move(this.position.getX(), this.position.getY());
        }

        return this.component;
    }

    public BiMap<String, Integer> getAllPorts() {
        BiMap<String, Integer> all = HashBiMap.create();
        all.putAll(this.inputTagsToIdsMap);
        all.putAll(this.outputTagsToIdsMap);
        return all;
    }
}
