package org.cafebabe.model.editor.workspace.circuit.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.cafebabe.model.IModel;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicState;
import org.cafebabe.model.editor.workspace.circuit.component.connection.LogicStateContainer;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Port;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.editor.workspace.circuit.component.position.TrackablePosition;
import org.cafebabe.model.editor.workspace.circuit.simulation.IScheduleStateEvents;
import org.cafebabe.model.util.EmptyEvent;

/**
 * This class represents a "Component" in the circuit
 * It has a set of inputs from which it can read logical values.
 * It has a set of outputs which logical values it can set (possibly based on the inputs' values).
 */
public abstract class Component implements IModel {

    @Getter protected Map<String, InputPort> tagToInput =
            Collections.unmodifiableMap(new HashMap<>());
    @Getter protected Map<String, OutputPort> tagToOutput =
            Collections.unmodifiableMap(new HashMap<>());

    /* TODO: Find out why this is offset is needed.
             Doesn't belong here, but will need to stay until we know. */
    private static final int VERTICAL_PORT_OFFSET = 28;
    @Getter private final EmptyEvent onDestroy = new EmptyEvent();
    @Getter private final TrackablePosition trackablePosition =
            new TrackablePosition(new Position(0, 0));
    @Getter private final String identifier;
    @Getter private final String displayName;
    @Getter private final String description;
    @Getter private EmptyEvent onUpdate = new EmptyEvent();

    public Component(String identifier, String displayName, String description) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }

    /* Public */
    public void connectToPort(Wire wire, String portTag) {
        if (this.tagToInput.containsKey(portTag)) {
            wire.connectInputPort(this.tagToInput.get(portTag));
        } else if (this.tagToOutput.containsKey(portTag)) {
            wire.connectOutputPort(this.tagToOutput.get(portTag));
        } else {
            throw new InvalidPortException("This port doesn't exist on this component");
        }
        updateOutputs();
    }

    public void disconnectFromPort(Wire wire, String portTag) {
        if (this.tagToInput.containsKey(portTag)) {
            wire.disconnectInputPort(this.tagToInput.get(portTag));
        } else if (this.tagToOutput.containsKey(portTag)) {
            wire.disconnectOutputPort(this.tagToOutput.get(portTag));
        } else {
            throw new InvalidPortException("This port doesn't exist on this component");
        }
        updateOutputs();
    }

    @Override
    public void destroy() {
        for (Port port : getPorts()) {
            port.destroy();
        }
        this.onDestroy.notifyListeners();
    }

    public void initPorts(ComponentData componentMetadata) {
        componentMetadata.inPortMetadata.forEach(p ->
                this.getPort(p.name).setPositionTracker(
                        this.trackablePosition.offsetClone((int) p.x,
                                (int) p.y + VERTICAL_PORT_OFFSET)
                )
        );
        componentMetadata.outPortMetadata.forEach(p ->
                this.getPort(p.name).setPositionTracker(
                        this.trackablePosition.offsetClone((int) p.x,
                                (int) p.y + VERTICAL_PORT_OFFSET)
                )
        );
    }

    public Port getPort(String portTag) {
        if (this.tagToOutput.containsKey(portTag)) {
            return this.tagToOutput.get(portTag);
        } else if (this.tagToInput.containsKey(portTag)) {
            return this.tagToInput.get(portTag);
        }
        throw new InvalidPortException("This port doesn't exist on this component");
    }

    public void setEventScheduler(IScheduleStateEvents eventScheduler) {
        for (LogicStateContainer stateContainer : getPorts()) {
            stateContainer.setEventScheduler(eventScheduler);
        }
    }

    /* Protected */
    protected void updateOutputs() {
        this.onUpdate.notifyListeners();
    }

    protected void setOutputState(OutputPort out, boolean state, List<InputPort> relatedInputs) {
        for (InputPort input : relatedInputs) {
            if (input.getLogicState() == LogicState.UNDEFINED) {
                out.setLogicState(LogicState.UNDEFINED);
                return;
            }
        }

        setOutputState(out, state);
    }

    /* Private */
    private void setOutputState(OutputPort out, boolean state) {
        out.setLogicState(state ? LogicState.HIGH : LogicState.LOW);
    }

    private List<Port> getPorts() {
        List<Port> ports = new ArrayList<>();
        ports.addAll(this.tagToInput.values());
        ports.addAll(this.tagToOutput.values());
        return ports;
    }
}
