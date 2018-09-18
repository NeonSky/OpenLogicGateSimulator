package model.util;

import org.cafebabe.model.components.AndGateComponent;
import org.cafebabe.model.components.PowerSourceComponent;
import org.cafebabe.model.util.ComponentUtil;
import org.junit.jupiter.api.Test;

import static org.cafebabe.model.util.ComponentUtil.componentFactory;
import static org.cafebabe.model.util.ComponentUtil.getAllComponents;
import static org.junit.jupiter.api.Assertions.*;

class ComponentUtilTest {

    @Test
    void getAllComponentsShouldReturnAllComponents() {
        assertDoesNotThrow(ComponentUtil::getAllComponents);

        assertTrue(getAllComponents().size() > 0);
    }

    @Test
    void componentFactoryShouldCreateComponents() {
        assertTrue(componentFactory("AND-Gate") instanceof AndGateComponent);
        assertTrue(componentFactory("Power Source") instanceof PowerSourceComponent);
    }

    @Test
    void componentFactoryShouldThrowOnBadInput() {
        assertThrows(RuntimeException.class, () -> componentFactory(""));
        assertThrows(RuntimeException.class, () -> componentFactory(null));
        assertThrows(RuntimeException.class, () -> componentFactory("foobarfoobarfoobarfoobarfoobar"));
    }

}