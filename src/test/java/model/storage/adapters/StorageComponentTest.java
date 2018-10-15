package model.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.storage.adapters.StorageComponent;
import org.junit.jupiter.api.Test;


class StorageComponentTest {

    @Test
    void createFromComponent() {
        AndGateComponent and = new AndGateComponent();

        var wrapper = new Object() {
            StorageComponent storageComponent;
        };
        assertDoesNotThrow(() -> wrapper.storageComponent = new StorageComponent(and));
        assertNotNull(wrapper.storageComponent);
    }

    @Test
    void creatingComponentWorks() {
        StorageComponent storageComponent = new StorageComponent(new AndGateComponent());

        var wrapper = new Object() {
            Component component;
        };
        assertDoesNotThrow(() -> wrapper.component = storageComponent.create());
        assertNotNull(wrapper.component);
        assertEquals(AndGateComponent.class, wrapper.component.getClass());
    }

    @Test
    void testGetAllPorts() {
        AndGateComponent and = new AndGateComponent();

        StorageComponent storageComponent = new StorageComponent(and);

        assertEquals(3, storageComponent.getAllPorts().size());
        assertTrue(storageComponent.getAllPorts().containsKey("input1"));
        assertTrue(storageComponent.getAllPorts().containsKey("input2"));
        assertTrue(storageComponent.getAllPorts().containsKey("output"));
    }

}
