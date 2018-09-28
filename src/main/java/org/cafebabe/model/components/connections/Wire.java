package org.cafebabe.model.components.connections;

import java.util.HashSet;
import java.util.Set;
import org.cafebabe.model.IReadOnlyMovable;
import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;

public class Wire extends LogicStateContainer implements IBelongToModel {

    private static final int MINIMUM_WIRE_CONNECTIONS = 2;

    public final Event<Position> onStartPosMoved = new Event<>();
    public final Event<Position> onEndPosMoved = new Event<>();

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final EmptyEvent onWillBeDestroyed = new EmptyEvent();
    private final Set<InputPort> connectedInputs;
    private final Set<OutputPort> connectedOutputs;
    private final Set<LogicStateContainer> powerSources;
    private final Set<LogicStateContainer> gndSources;
    private boolean destructionPending;
    private IReadOnlyMovable trackableStartPos;
    private IReadOnlyMovable trackableEndPos;


    public Wire() {
        onStateChanged = new Event<>();
        connectedInputs = new HashSet<>();
        connectedOutputs = new HashSet<>();
        powerSources = new HashSet<>();
        gndSources = new HashSet<>();
    }

    /* Public */

    /**
     * Connects an InputPort if it isn't already connected,
     * otherwise throws a RuntimeException.
     */
    public void connectInputPort(InputPort input) {
        if (isInputConnected(input)) {
            throw new RuntimeException("An InputPort can only be added once.");
        }
        input.onWillBeDestroyed().addListener(this::onConnectedInputPortDestroyed);
        input.setStateSource(this);
        connectedInputs.add(input);
        setTrackableEndPos(input.getPositionTracker());
    }

    /**
     * Connects an OutputPort if it isn't already connected,
     * otherwise throws a RuntimeException.
     */
    public void connectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if (isOutputConnected(output)) {
                throw new RuntimeException("An OutputPort can only be added once.");
            }

            output.setConnected(true);
            connectedOutputs.add(output);
            if (output.isHigh()) {
                powerSources.add(output);
            }
            if (output.isLow()) {
                gndSources.add(output);
            }

            output.onWillBeDestroyed().addListener(this::onConnectedOutputPortDestroyed);
            output.onStateChangedEvent().addListener(this::onConnectedOutputStateChanged);
            setTrackableStartPos(output.getPositionTracker());
        });
    }

    /**
     * Disconnects an InputPort if it is connected,
     * otherwise throws a RuntimeException.
     */
    public void disconnectInputPort(InputPort input) {
        if (!isInputConnected(input)) {
            throw new RuntimeException("An InputPort that isn't connected can't be removed.");
        }
        input.onStateChanged.removeListener(this::onConnectedOutputStateChanged);
        input.onWillBeDestroyed().removeListener(this::onConnectedInputPortDestroyed);
        input.removeStateSource();
        connectedInputs.remove(input);
    }

    @Override
    public void destroy() {
        if (destructionPending) {
            return;
        }
        destructionPending = true;
        disconnectAll();
        onWillBeDestroyed.notifyListeners();
    }

    public void disconnectAll() {
        notifyIfStateChanges(() -> {
            for (InputPort inport : this.connectedInputs) {
                disconnectInputPort(inport);
            }
            for (OutputPort outport : this.connectedOutputs) {
                disconnectOutputPort(outport);
            }
        });
    }

    /**
     * Disconnects an OutputPort if it is connected,
     * otherwise throws a RuntimeException.
     */
    public void disconnectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if (!isOutputConnected(output)) {
                throw new RuntimeException("An OutputPort that isn't connected can't be removed.");
            }
            output.onWillBeDestroyed().removeListener(this::onConnectedOutputPortDestroyed);
            output.onStateChangedEvent().removeListener(this::onConnectedOutputStateChanged);
            output.setConnected(false);
            connectedOutputs.remove(output);
            powerSources.remove(output);
            gndSources.remove(output);
        });
    }

    public EmptyEvent onWillBeDestroyed() {
        return onWillBeDestroyed;
    }

    @Override
    public LogicState logicState() {
        if (powerSources.isEmpty() && gndSources.isEmpty()) {
            return LogicState.UNDEFINED;
        }
        if (powerSources.isEmpty()) {
            return LogicState.LOW;
        }
        return LogicState.HIGH;
    }

    public boolean isAnyInputConnected() {
        return !connectedInputs.isEmpty();
    }

    public boolean isAnyOutputConnected() {
        return !connectedOutputs.isEmpty();
    }

    /* Private */

    /**
     * Updates the wire's logical value based on updated output value.
     */
    private void onConnectedOutputStateChanged(LogicStateContainer updatedPort) {
        if (!connectedOutputs.contains(updatedPort)) {
            throw new RuntimeException("Updated port isn't connected!");
        }
        notifyIfStateChanges(() -> {
            switch (updatedPort.logicState()) {
                case LOW:
                    gndSources.add(updatedPort);
                    powerSources.remove(updatedPort);
                    break;
                case HIGH:
                    gndSources.remove(updatedPort);
                    powerSources.add(updatedPort);
                    break;
                case UNDEFINED:
                    gndSources.remove(updatedPort);
                    powerSources.remove(updatedPort);
                    break;
                default:
            }
        });
    }

    private boolean isInputConnected(InputPort input) {
        return connectedInputs.contains(input);
    }

    private void setTrackableEndPos(IReadOnlyMovable trackableEndPos) {
        if (this.trackableEndPos != null) {
            this.trackableEndPos.removePositionListener(this.onEndPosMoved::notifyListeners);
        }
        this.trackableEndPos = trackableEndPos;
        this.trackableEndPos.addPositionListener(this.onEndPosMoved::notifyListeners);
        onEndPosMoved.notifyListeners(new Position(trackableEndPos.getX(), trackableEndPos.getY()));
    }

    private boolean isOutputConnected(OutputPort output) {
        return connectedOutputs.contains(output);
    }

    private void setTrackableStartPos(IReadOnlyMovable trackableStartPos) {
        if (this.trackableStartPos != null) {
            this.trackableStartPos.removePositionListener(this.onStartPosMoved::notifyListeners);
        }
        this.trackableStartPos = trackableStartPos;
        this.trackableStartPos.addPositionListener(this.onStartPosMoved::notifyListeners);
        onStartPosMoved.notifyListeners(
                new Position(trackableStartPos.getX(), trackableStartPos.getY())
        );
    }

    private void onConnectedInputPortDestroyed(InputPort port) {
        if (isInputConnected(port)) {
            disconnectInputPort(port);
        }
        if (connectedPortsCount() < MINIMUM_WIRE_CONNECTIONS) {
            destroy();
        }
    }

    private int connectedPortsCount() {
        return connectedInputs.size() + connectedOutputs.size();
    }

    private void onConnectedOutputPortDestroyed(OutputPort port) {
        if (isOutputConnected(port)) {
            disconnectOutputPort(port);
        }
        if (connectedPortsCount() < MINIMUM_WIRE_CONNECTIONS) {
            destroy();
        }
    }
}
