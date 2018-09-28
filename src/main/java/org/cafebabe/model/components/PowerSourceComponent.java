package org.cafebabe.model.components;

import java.util.Map;
import org.cafebabe.model.components.connections.OutputPort;

public class PowerSourceComponent extends Component {

    @ComponentConstructor
    public PowerSourceComponent() {
        OutputPort northOutput = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", northOutput)
        );

        setOutputState(northOutput, true);
    }

    /* Public */
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

    @Override
    protected void update() {
    }

}
