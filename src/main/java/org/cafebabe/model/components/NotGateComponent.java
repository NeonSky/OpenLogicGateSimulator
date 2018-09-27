package org.cafebabe.model.components;

import java.util.Collections;
import java.util.Map;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;


public class NotGateComponent extends Component {

    private final InputPort input;
    private final OutputPort output;

    @ComponentConstructor
    public NotGateComponent() {
        input = new InputPort();
        TAG_TO_INPUT = Map.ofEntries(
                Map.entry("input", input)
        );

        output = new OutputPort();
        TAG_TO_OUTPUT = Map.ofEntries(
                Map.entry("output", output)
        );

        input.onStateChangedEvent().addListener(p -> update());
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "NOT_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NOT Gate";
    }

    @Override
    public String getDescription() {
        return "Emits the opposite signal of the input";
    }

    @Override
    protected void update() {
        setOutputState(output, !input.isHigh(), Collections.singletonList(input));
    }

}
