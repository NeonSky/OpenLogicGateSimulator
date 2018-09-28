package model.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cafebabe.model.workspace.Position;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void newPositionNoParamsShouldBeOrigin() {
        Position p = new Position();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);
    }

    @Test
    void newPositionShouldBePositionedByParams() {
        Position p = new Position(1, 1);
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);

        p = new Position(-1, -1);
        assertEquals(p.getX(), -1);
        assertEquals(p.getY(), -1);

    }
}
