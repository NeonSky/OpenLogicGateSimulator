package org.cafebabe.model.editor.workspace.circuit.component.feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * A component that displays a hexadecimal representation of the given binary number.
 */
public class HexDisplayComponent extends Component {

    private static final int[] INT_TO_SEG = new int[] {
            0x3F, 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07,
            0x7F, 0x67, 0x77, 0x7C, 0x39, 0x5E, 0x79, 0x71
    };
    private final List<InputPort> inputs = new ArrayList<>();

    @ComponentConstructor
    public HexDisplayComponent() {
        super("HEX_Display", "Hex Display", "Displays a hexadecimal representation of its input.");
        tagToInput = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            addInputAtIndex(i);
        }
    }

    /* Public */
    public boolean isSegmentActive(int i) {
        int number = getCurrentValue();
        return ((INT_TO_SEG[number] >> i) & 1) == 1;
    }

    /* Private */
    private int getCurrentValue() {
        int number = 0;
        for (int i = 0; i < 4; i++) {
            number += this.inputs.get(i).isHigh() ? 1 << i : 0;
        }
        return number;
    }

    private void addInputAtIndex(int i) {
        InputPort port = new InputPort();
        tagToInput.put("input" + i, port);
        this.inputs.add(i, port);
        port.onStateChangedEvent().addListener(p -> getOnUpdate().notifyListeners());
    }
}
