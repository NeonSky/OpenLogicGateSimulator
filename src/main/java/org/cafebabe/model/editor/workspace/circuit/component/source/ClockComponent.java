package org.cafebabe.model.editor.workspace.circuit.component.source;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.IDynamicComponent;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.simulation.DynamicEvent;
import org.cafebabe.model.util.Event;

/**
 * An component that only has one output. This output switches back and
 * forth between high and low state over time.
 */
public class ClockComponent extends Component implements IDynamicComponent {

    private final OutputPort output;
    @Getter private final Event<DynamicEvent> onNewDynamicEvent;
    private boolean shouldIDie;


    @ComponentConstructor
    public ClockComponent() {
        super("SOURCE_Clock", "Clock", "Switches back and forth between high and low output.");
        this.onNewDynamicEvent = new Event<>();
        this.output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output)
        );
        this.output.setLogicState(LogicState.LOW);

        this.getOnDestroy().addListener(() -> this.shouldIDie = true);
    }


    /* Public */
    @Override
    public void updateOutputs() {}

    @Override
    public boolean shouldDie() {
        return this.shouldIDie;
    }

    @Override
    public List<DynamicEvent> getInitialDynamicEvents() {
        return Arrays.asList(new DynamicEvent(this, 1000, this::changePulse));
    }


    /* Private */
    private List<DynamicEvent> changePulse() {
        if (this.output.getLogicState() == LogicState.LOW) {
            this.output.setLogicState(LogicState.HIGH);
        } else {
            this.output.setLogicState(LogicState.LOW);
        }
        return Arrays.asList(new DynamicEvent(this, 1000, this::changePulse));
    }
}
