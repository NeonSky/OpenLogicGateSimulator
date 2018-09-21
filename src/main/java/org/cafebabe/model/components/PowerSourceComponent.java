package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Wire;

import java.util.Map;

public class PowerSourceComponent extends Component {

    private OutputPort northOutput;

    @ComponentConstructor
    public PowerSourceComponent() {
        northOutput = new OutputPort();
        TAG_TO_OUTPUT = java.util.Map.ofEntries(
            Map.entry("output", northOutput)
        );

        northOutput.setActive(true);
    }

    @Override
    protected void update() {}

    @Override
    public String getAnsiName() {
        return "PowerSource";
    }

    @Override
    public String getDisplayName() {
        return "Power Source";
    }

    @Override
    public String getDescription() {
        return "Emits an active signal to all connected outputs";
    }

}
