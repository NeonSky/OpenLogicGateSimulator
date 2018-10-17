package org.cafebabe.model.editor.workspace.circuit.component.feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * A component with 7 sements that can be individually lit / unlit.
 */
public class SegmentDisplayComponent extends Component {

    private final List<InputPort> inputs = new ArrayList<>();

    @ComponentConstructor
    public SegmentDisplayComponent() {
        super("7SEG_Display", "7 Segment Display", "A display with seven segments.");
        tagToInput = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            addInputAtIndex(i);
        }
    }

    /* Protected */
    @Override
    protected void updateOutputs() {
        for (int i = 0; i < 7; i++) {
            InputPort port = this.inputs.get(i);
            if (port.isHigh()) {
                addStateData("port-" + i + "-high");
            } else {
                removeStateData("port-" + i + "-high");
            }
        }
    }

    /* Private */
    private void addInputAtIndex(int i) {
        InputPort port = new InputPort();
        tagToInput.put("input" + i, port);
        this.inputs.add(i, port);
        port.onStateChangedEvent().addListener(p -> updateOutputs());
        removeStateData("port-" + i + "-high");
    }
}