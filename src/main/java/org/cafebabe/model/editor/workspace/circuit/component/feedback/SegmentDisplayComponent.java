package org.cafebabe.model.editor.workspace.circuit.component.feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * A component with 7 segments that can be individually lit / unlit.
 */
public class SegmentDisplayComponent extends Component {

    private final List<InputPort> inputs = new ArrayList<>();

    @ComponentConstructor
    public SegmentDisplayComponent() {
        super("FEEDBACK_7SegDisplay", "7 Segment Display", "A display with seven segments.");
        tagToInput = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            addInputAtIndex(i);
        }
    }

    /* Public */
    public boolean isSegmentActive(int i) {
        return this.inputs.get(i).isHigh();
    }

    /* Private */
    private void addInputAtIndex(int i) {
        InputPort port = new InputPort();
        tagToInput.put("input" + i, port);
        this.inputs.add(i, port);
        port.getOnStateChanged().addListener(p -> this.getOnUpdate().notifyListeners());
    }
}
