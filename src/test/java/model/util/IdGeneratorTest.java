package model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cafebabe.model.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class IdGeneratorTest {

    @BeforeEach
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void resetCounter() {
        IdGenerator.setStartingValue(0);
    }

    @Test
    void counterStartsAtZero() {
        assertEquals(0, IdGenerator.getNewId());
    }

    @Test
    void counterIsSequential() {
        int first = IdGenerator.getNewId();
        int second = IdGenerator.getNewId();

        assertTrue(first < second);
    }

    @Test
    void counterSetWorks() {
        int start = 10;
        IdGenerator.setStartingValue(start);
        assertEquals(start, IdGenerator.getNewId());
    }

    @Test
    void settingCounterToNegativeShouldSetToZero() {
        int start = -10;
        IdGenerator.setStartingValue(start);
        assertEquals(0, IdGenerator.getNewId());
    }
}
