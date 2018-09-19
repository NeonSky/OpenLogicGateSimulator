package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;


public class NotGateComponent extends Component {

    private InputPort input;
    private OutputPort output;

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

    @Override
    protected void update() {
        System.out.println(!input.isActive());
        output.setActive(!input.isActive());
    }

    @Override
    public String getAnsiName() {
        return "NOT_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NOT-Gate";
    }

    @Override
    public String getDescription() {
        return "Emits the opposite signal of the input";
    }

}
