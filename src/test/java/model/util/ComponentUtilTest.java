package model.util;

import static org.cafebabe.model.editor.util.ComponentUtil.componentFactory;
import static org.cafebabe.model.editor.util.ComponentUtil.getAllComponents;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.editor.util.ComponentUtil;
import org.cafebabe.model.editor.workspace.circuit.component.gate.AndGateComponent;
import org.cafebabe.model.editor.workspace.circuit.component.source.SignalSourceComponent;
import org.junit.jupiter.api.Test;


class ComponentUtilTest {

    /* Package-Private */
    @Test
    void gettingAllComponentsShouldReturnAllComponents() {
        assertDoesNotThrow(ComponentUtil::getAllComponents);

        assertTrue(getAllComponents().size() > 0);
    }

    @Test
    void componentFactoryShouldCreateComponents() {
        assertTrue(componentFactory("AND Gate") instanceof AndGateComponent);
        assertTrue(componentFactory("Signal Source") instanceof SignalSourceComponent);
    }

    @Test
    void componentFactoryShouldThrowOnBadInput() {
        assertThrows(RuntimeException.class, () -> componentFactory(""));
        assertThrows(RuntimeException.class, () -> componentFactory(null));
        assertThrows(RuntimeException.class, () -> componentFactory("foobarfoobarfoobarfoobar"));
    }

}