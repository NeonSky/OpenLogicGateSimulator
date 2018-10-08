package org.cafebabe.model.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cafebabe.controllers.util.Metadata;
import org.cafebabe.model.IDestructible;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.circuit.simulation.IScheduleStateEvents;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.LogicState;
import org.cafebabe.model.components.connections.LogicStateContainer;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;
import org.cafebabe.util.EmptyEvent;

/**
 * This class represents a "Component" in the circuit
 * It has a set of inputs from which it can read logical values.
 * It has a set of outputs which logical values it can set (possibly based on the inputs' values).
 */
public abstract class Component implements IBelongToModel, IDestructible {

    //TODO: Find out why this is offset is needed
    private static final int VERTICAL_PORT_OFFSET = 28;
    private final EmptyEvent onDestroy = new EmptyEvent();
    private final TrackablePosition trackablePosition = new TrackablePosition(new Position(0, 0));
    Map<String, InputPort> tagToInput = Collections.unmodifiableMap(new HashMap<>());
    Map<String, OutputPort> tagToOutput = Collections.unmodifiableMap(new HashMap<>());

    /* Public */
    @Override
    public EmptyEvent getOnDestroy() {
        return this.onDestroy;
    }

    public void connectToPort(Wire wire, String portTag) {
        if (this.tagToInput.containsKey(portTag)) {
            wire.connectInputPort(this.tagToInput.get(portTag));
        } else if (this.tagToOutput.containsKey(portTag)) {
            wire.connectOutputPort(this.tagToOutput.get(portTag));
        } else {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        update();
    }

    public void disconnectFromPort(Wire wire, String portTag) {
        if (this.tagToInput.containsKey(portTag)) {
            wire.disconnectInputPort(this.tagToInput.get(portTag));
        } else if (this.tagToOutput.containsKey(portTag)) {
            wire.disconnectOutputPort(this.tagToOutput.get(portTag));
        } else {
            throw new RuntimeException("This port doesn't exist on this component");
        }
        update();
    }

    @Override
    public void destroy() {
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
        for (Map.Entry<String, InputPort> entry : this.tagToInput.entrySet()) {
            entry.getValue().destroy();
        }
        for (Map.Entry<String, OutputPort> entry : this.tagToOutput.entrySet()) {
            entry.getValue().destroy();
        }
    }

    public abstract String getAnsiName();

    public abstract String getDisplayName();

    public abstract String getDescription();

    public TrackablePosition getTrackablePosition() {
        return this.trackablePosition;
    }

    public void initPorts(Metadata componentMetadata) {
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
        throw new RuntimeException("This port doesn't exist on this component");
    }

    public void setEventScheduler(IScheduleStateEvents eventScheduler) {
        for (LogicStateContainer stateContainer : getPorts()) {
            stateContainer.setEventScheduler(eventScheduler);
        }
    }


    /* Package-Private */
    void setOutputState(OutputPort out, boolean state, List<InputPort> relatedInputs) {
        for (InputPort input : relatedInputs) {
            if (input.logicState() == LogicState.UNDEFINED) {
                out.setState(LogicState.UNDEFINED);
                return;
            }
        }

        setOutputState(out, state);
    }

    void setOutputState(OutputPort out, boolean state) {
        out.setState(state ? LogicState.HIGH : LogicState.LOW);
    }

    /* Protected */
    protected abstract void update();

    /* Private */
    private List<Port> getPorts() {
        List<Port> ports = new ArrayList<>();
        ports.addAll(this.tagToInput.values());
        ports.addAll(this.tagToOutput.values());
        return ports;
    }

}
