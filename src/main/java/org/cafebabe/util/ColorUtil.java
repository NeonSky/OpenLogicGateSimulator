package org.cafebabe.util;

import java.util.Map;
import javafx.scene.paint.Color;
import org.cafebabe.model.components.connections.LogicState;

public class ColorUtil {

    public static final Color SELECTED = Color.color(80.0 / 255, 144.0 / 255, 186.0 / 255, 1);
    public static final Color GRID_LINE = Color.LIGHTGRAY;
    public static final Color BLACK = Color.BLACK;
    public static final Color OFFWHITE = Color.color(239.0 / 255, 239.0 / 255, 239.0 / 255, 0.85);
    public static final Color TRANSPARENT = Color.TRANSPARENT;

    private static final Color ACTIVE = Color.color(234.0 / 255, 38.0 / 255, 38.0 / 255, 1);
    private static final Color INACTIVE = Color.color(0, 0, 0, 1);
    private static final Color UNDEFINED = Color.color(204.0 / 255, 204.0 / 255, 0, 1);


    private static final Map<LogicState, Color> STATE_TO_COLOR = Map.ofEntries(
            Map.entry(LogicState.HIGH, ColorUtil.ACTIVE),
            Map.entry(LogicState.LOW, ColorUtil.INACTIVE),
            Map.entry(LogicState.UNDEFINED, ColorUtil.UNDEFINED)
    );

    /* Public */
    public static Color getStateColor(LogicState state) throws RuntimeException {
        if (STATE_TO_COLOR.containsKey(state)) {
            return STATE_TO_COLOR.get(state);
        } else {
            throw new RuntimeException("No color mapped to state: " + state.toString());
        }
    }
}
