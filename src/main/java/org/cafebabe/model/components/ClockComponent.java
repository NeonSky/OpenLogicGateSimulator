package org.cafebabe.model.components;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.cafebabe.model.IDynamicComponent;
import org.cafebabe.model.circuit.simulation.DynamicEvent;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.OutputPort;

/**
 * An component that only has one output. This output switches back and
 * forth between high and low state over time.
 */
public class ClockComponent extends Component implements IDynamicComponent {

    private final OutputPort output;
    private boolean shouldIDie;


    @ComponentConstructor
    public ClockComponent() {
        this.output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output)
        );
        this.output.setState(LogicState.LOW);

        this.getOnDestroy().addListener(() -> this.shouldIDie = true);
    }


    /* Public */
    @Override
    public void update() {}

    @Override
    public boolean shouldDie() {
        return this.shouldIDie;
    }

    @Override
    public List<DynamicEvent> getInitialDynamicEvents() {
        return Arrays.asList(new DynamicEvent(this, 1000, this::changePulse));
    }

    @Override
    public String getAnsiName() {
        return "CLOCK_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "Ping-Pong pulse emitter.";
    }

    @Override
    public String getDescription() {
        return "Switches back and forth between high and low output.";
    }


    /* Private */
    private List<DynamicEvent> changePulse() {
        if (this.output.logicState() == LogicState.LOW) {
            this.output.setState(LogicState.HIGH);
        } else {
            this.output.setState(LogicState.LOW);
        }
        return Arrays.asList(new DynamicEvent(this, 1000, this::changePulse));
    }
}
