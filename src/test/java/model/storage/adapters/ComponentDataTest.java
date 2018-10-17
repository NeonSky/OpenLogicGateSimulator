package model.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.HashBiMap;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.storage.adapters.ComponentData;
import org.junit.jupiter.api.Test;

class ComponentDataTest {

    @Test
    void noDataShouldBeInvalid() {
        assertFalse(new ComponentData().isValid());
    }

    @Test
    void allDataShouldBeValid() {
        ComponentData data = new ComponentData();
        data.setIdentifier("display name");
        data.setPosition(new Position(10, 20));
        data.setInputIds(HashBiMap.create());
        data.setOutputIds(HashBiMap.create());

        assertTrue(data.isValid());
    }
}