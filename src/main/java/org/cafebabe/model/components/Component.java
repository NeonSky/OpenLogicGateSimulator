package org.cafebabe.model.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cafebabe.controllers.util.Metadata;
import org.cafebabe.model.IDestructible;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.components.connections.*;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.model.workspace.TrackablePosition;
import org.cafebabe.util.EmptyEvent;

public abstract class Component implements IBelongToModel, IDestructible {

    private static final int VERTICAL_PORT_OFFSET = 28; //TODO: Find out why this is offset is needed
    private final EmptyEvent onDestroy = new EmptyEvent();
    private final TrackablePosition trackablePosition = new TrackablePosition(new Position(0, 0));
    Map<String, InputPort> TAG_TO_INPUT = Collections.unmodifiableMap(new HashMap<>());
    Map<String, OutputPort> TAG_TO_OUTPUT = Collections.unmodifiableMap(new HashMap<>());

    /* Public */
    public EmptyEvent getOnDestroy() {
        return onDestroy;
    }

    public void connectToPort(Wire wire, String portTag) {
        if (TAG_TO_INPUT.containsKey(portTag)) wire.connectInputPort(TAG_TO_INPUT.get(portTag));
        else if (TAG_TO_OUTPUT.containsKey(portTag)) wire.connectOutputPort(TAG_TO_OUTPUT.get(portTag));
        else throw new RuntimeException("This port doesn't exist on this component");
        update();
    }

    public void disconnectFromPort(Wire wire, String portTag) {
        if (TAG_TO_INPUT.containsKey(portTag)) wire.disconnectInputPort(TAG_TO_INPUT.get(portTag));
        else if (TAG_TO_OUTPUT.containsKey(portTag)) wire.disconnectOutputPort(TAG_TO_OUTPUT.get(portTag));
        else throw new RuntimeException("This port doesn't exist on this component");
        update();
    }

    public void destroy() {
        this.onDestroy.notifyListeners();
        this.onDestroy.removeListeners();
        for (Map.Entry<String, InputPort> entry : TAG_TO_INPUT.entrySet()) {
            entry.getValue().destroy();
        }
        for (Map.Entry<String, OutputPort> entry : TAG_TO_OUTPUT.entrySet()) {
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
                this.getPort(p.name).setPositionTracker(this.trackablePosition.offsetClone((int) p.x, (int) p.y + VERTICAL_PORT_OFFSET))
        );
        componentMetadata.outPortMetadata.forEach(p ->
                this.getPort(p.name).setPositionTracker(this.trackablePosition.offsetClone((int) p.x, (int) p.y + VERTICAL_PORT_OFFSET))
        );
    }

    public Port getPort(String portTag) {
        if (TAG_TO_OUTPUT.containsKey(portTag)) return TAG_TO_OUTPUT.get(portTag);
        else if (TAG_TO_INPUT.containsKey(portTag)) return TAG_TO_INPUT.get(portTag);
        throw new RuntimeException("This port doesn't exist on this component");
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

    /* Private */
    protected abstract void update();

}
