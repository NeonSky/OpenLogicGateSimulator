package model.util;

import static org.cafebabe.model.editor.util.ComponentUtil.componentFactory;
import static org.cafebabe.model.editor.util.ComponentUtil.getAllComponents;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.util.ComponentUtil;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.PowerSourceComponent;
import org.junit.jupiter.api.Test;


class ComponentUtilTest {

    @Test
    void gettingAllComponentsShouldReturnAllComponents() {
        assertDoesNotThrow(ComponentUtil::getAllComponents);

        assertTrue(getAllComponents().size() > 0);
    }

    @Test
    void componentFactoryShouldCreateComponents() {
        assertTrue(componentFactory("AND_Gate") instanceof AndGateComponent);
        assertTrue(componentFactory("POWER_Source") instanceof PowerSourceComponent);
    }

    @Test
    void componentFactoryShouldThrowOnBadInput() {
        assertThrows(RuntimeException.class, () -> componentFactory(""));
        assertThrows(RuntimeException.class, () -> componentFactory(null));
        assertThrows(RuntimeException.class, () -> componentFactory("foobarfoobarfoobarfoobar"));
    }

}