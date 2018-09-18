package model.workspace;

import org.cafebabe.model.workspace.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void positionShouldBeMovable() {
        Position p = new Position();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);

        p.move(10, 10);
        assertEquals(p.getX(), 10);
        assertEquals(p.getY(), 10);
    }

    @Test
    void positionShouldBeTranslatable() {
        Position p = new Position();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);

        for (int i=1; i<=10; i++) {
            p.translate(10, -10);
            assertEquals(p.getX(), i*10);
            assertEquals(p.getY(), -i*10);
        }
    }

}
