package model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class IdGeneratorTest {

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.reset();
    }

    @Test
    void counterStartsAtZero() {
        assertEquals(0, IdGenerator.getNewId());
    }

    @Test
    void counterResetWorks() {
        for (int i = 0; i < 10; i++) {
            IdGenerator.getNewId();
        }
        IdGenerator.reset();
        assertEquals(0, IdGenerator.getNewId());
    }
}
