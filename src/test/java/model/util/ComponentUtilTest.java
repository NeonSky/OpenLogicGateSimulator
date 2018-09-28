package model.util;

import static org.cafebabe.model.util.ComponentUtil.componentFactory;
import static org.cafebabe.model.util.ComponentUtil.getAllComponents;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.util.ComponentUtil;
import org.junit.jupiter.api.Test;


class ComponentUtilTest {

    @Test
    void gettingAllComponentsShouldReturnAllComponents() {
        assertDoesNotThrow(ComponentUtil::getAllComponents);

        assertTrue(getAllComponents().size() > 0);
    }

    @Test
    void componentFactoryShouldCreateComponents() {
        assertTrue(componentFactory("AND Gate") instanceof AndGateComponent);
        assertTrue(componentFactory("Power Source") instanceof PowerSourceComponent);
    }

    @Test
    void componentFactoryShouldThrowOnBadInput() {
        assertThrows(RuntimeException.class, () -> componentFactory(""));
        assertThrows(RuntimeException.class, () -> componentFactory(null));
        assertThrows(RuntimeException.class, () -> componentFactory("foobarfoobarfoobarfoobar"));
    }

}