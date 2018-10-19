package org.cafebabe.model.editor.workspace.circuit.component.gate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.IDynamicComponent;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.simulation.DynamicEvent;
import org.cafebabe.model.util.Event;

/**
 * A component which mimics its input with a 1 second delay.
 */
public class DelayComponent extends Component implements IDynamicComponent {

    private final OutputPort output;
    private final InputPort input;
    @Getter private final Event<DynamicEvent> onNewDynamicEvent;
    private boolean shouldIDie;


    @ComponentConstructor
    public DelayComponent() {
        super("GATE__Delay", "Delay", "Delays the input signal by a second");
        this.onNewDynamicEvent = new Event<>();
        this.input = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input", this.input)
        );
        this.output = new OutputPort();
        tagToOutput = Map.ofEntries(
                Map.entry("output", this.output)
        );

        this.input.getOnStateChanged().addListener(e -> updateOutputs());
        this.getOnDestroy().addListener(() -> this.shouldIDie = true);
    }


    /* Public */
    @Override
    public void updateOutputs() {
        LogicState currentInput = this.input.getLogicState();
        this.onNewDynamicEvent.notifyListeners(
                new DynamicEvent(this, 1000,
                        () -> {
                            this.output.setLogicState(currentInput);
                            return Collections.emptyList();
                        }
                )
        );
    }

    @Override
    public boolean shouldDie() {
        return this.shouldIDie;
    }

    @Override
    public List<DynamicEvent> getInitialDynamicEvents() {
        return Collections.emptyList();
    }
}
