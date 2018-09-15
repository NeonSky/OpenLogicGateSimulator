package model.workspace;

import org.cafebabe.model.workspace.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    @Test
    void positionNoParams() {
        Position p = new Position();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);
    }

    @Test
    void positionParams() {
        Position p = new Position(1, 1);
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);

        p = new Position(-1, -1);
        assertEquals(p.getX(), -1);
        assertEquals(p.getY(), -1);

    }

    @Test
    void positionMove() {
        Position p = new Position();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);

        p.move(10, 10);
        assertEquals(p.getX(), 10);
        assertEquals(p.getY(), 10);
    }

}
