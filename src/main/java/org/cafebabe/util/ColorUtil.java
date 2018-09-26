package org.cafebabe.util;

import javafx.scene.paint.Color;
import org.cafebabe.model.components.connections.LogicState;

import java.util.Map;

public class ColorUtil {
    public static final Color ACTIVE = Color.color(234.0/255, 38.0/255, 38.0/255, 1);
    public static final Color INACTIVE = Color.color(0, 0, 0, 1);
    public static final Color SELECTED = Color.color(80.0/255, 144.0/255, 186.0/255, 1);
    public static final Color UNDEFINED = Color.color(204.0/255, 204.0/255, 0, 1);

    public static final Color getStateColor(LogicState state) throws RuntimeException {
        if (STATE_TO_COLOR.containsKey(state)) {
            return STATE_TO_COLOR.get(state);
        } else {
            throw new RuntimeException("Attempting to get color with unmapped LogicState.");
        }
    }

    private static final Map<LogicState, Color> STATE_TO_COLOR = Map.ofEntries(
        Map.entry(LogicState.HIGH, ColorUtil.ACTIVE),
        Map.entry(LogicState.LOW, ColorUtil.INACTIVE),
        Map.entry(LogicState.UNDEFINED, ColorUtil.UNDEFINED)
    );
}
