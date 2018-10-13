package model.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cafebabe.model.editor.workspace.Position;
import org.cafebabe.model.editor.workspace.TrackablePosition;
import org.junit.jupiter.api.Test;

class TrackablePositionTest {
    /* Package-Private */
    @Test
    void newTrackablePositionNoParamsShouldBeOrigin() {
        TrackablePosition p = new TrackablePosition(new Position());
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);
    }

    @Test
    void newTrackablePositionShouldBePositionedByParams() {
        TrackablePosition p = new TrackablePosition(new Position(1, 1));
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);

        p = new TrackablePosition(new Position(-1, -1));
        assertEquals(p.getX(), -1);
        assertEquals(p.getY(), -1);

    }

    @Test
    void trackablePositionShouldBeMovable() {
        TrackablePosition p = new TrackablePosition(new Position());
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);

        p.move(10, 10);
        assertEquals(p.getX(), 10);
        assertEquals(p.getY(), 10);
    }

    @Test
    void trackablePositionShouldBeTranslatable() {
        TrackablePosition p = new TrackablePosition(new Position());
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 0);

        for (int i = 1; i <= 10; i++) {
            p.translate(10, -10);
            assertEquals(p.getX(), i * 10);
            assertEquals(p.getY(), -i * 10);
        }
    }
}
