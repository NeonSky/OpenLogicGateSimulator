package org.cafebabe.util;

import java.util.Map;
import javafx.scene.paint.Color;
import org.cafebabe.model.components.connections.LogicState;

@SuppressWarnings("checkstyle:MethodParamPad")
public enum ColorUtil {
    SELECTED (Color.color(80.0 / 255, 144.0 / 255, 186.0 / 255, 1)),
    SELECTION_BOX_STROKE (Color.color(80.0 / 255,144.0 / 255, 200.0 / 255, 1)),
    SELECTION_BOX_FILL (Color.color(80.0 / 255, 144.0 / 255, 200.0 / 255, 0.1)),
    GRID_LINE (Color.LIGHTGRAY),
    BLACK (Color.BLACK),
    OFFWHITE (Color.color(239.0 / 255, 239.0 / 255, 239.0 / 255, 0.85)),
    TRANSPARENT (Color.TRANSPARENT),
    ACTIVE (Color.color(234.0 / 255, 38.0 / 255, 38.0 / 255, 1)),
    INACTIVE (Color.color(0, 0, 0, 1)),
    UNDEFINED (Color.color(204.0 / 255, 204.0 / 255, 0, 1));

    private final Color color;
    private static final Map<LogicState, ColorUtil> STATE_TO_COLOR = Map.ofEntries(
            Map.entry(LogicState.HIGH, ColorUtil.ACTIVE),
            Map.entry(LogicState.LOW, ColorUtil.INACTIVE),
            Map.entry(LogicState.UNDEFINED, ColorUtil.UNDEFINED)
    );

    ColorUtil(Color color) {
        this.color = color;
    }

    /* Public */
    public Color color() {
        return this.color;
    }

    public static Color getStateColor(LogicState state) throws RuntimeException {
        if (STATE_TO_COLOR.containsKey(state)) {
            return STATE_TO_COLOR.get(state).color();
        } else {
            throw new RuntimeException("No color mapped to state: " + state.toString());
        }
    }
}
