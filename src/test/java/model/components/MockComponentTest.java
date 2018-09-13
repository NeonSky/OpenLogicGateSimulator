package model.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.cafebabe.model.components.MockComponent;

class MockComponentTest {

    @Test
    void metadataTest() {
        MockComponent mockComponent = new MockComponent();
        assertEquals("Buffer_ANSI", mockComponent.getAnsiName());
        assertEquals("Super Cool Mock Component", mockComponent.getDisplayName());
        assertEquals("Delays the signal by one tick", mockComponent.getDescription());
    }

}
