package org.cafebabe.util;

import java.util.Map;
import javafx.scene.paint.Color;
import org.cafebabe.model.components.connections.LogicState;

/**
 * Defines the application's color scheme with a collection of color constants.
 */
public final class ColorUtil {

    public static final Color SELECTED = Color.color(80.0 / 255, 144.0 / 255, 186.0 / 255, 1);
    @SuppressWarnings("checkstyle:linelength")
    public static final Color SELECTION_BOX_STROKE = Color.color(80.0 / 255,144.0 / 255, 200.0 / 255, 1);
    public static final Color SELECTION_BOX_FILL = Color.color(80.0 / 255, 144.0 / 255, 200.0 / 255, 0.1);
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

    private ColorUtil() {}

    /* Public */
    public static Color getStateColor(LogicState state) throws RuntimeException {
        if (STATE_TO_COLOR.containsKey(state)) {
            return STATE_TO_COLOR.get(state);
        } else {
            throw new RuntimeException("No color mapped to state: " + state.toString());
        }
    }
}
