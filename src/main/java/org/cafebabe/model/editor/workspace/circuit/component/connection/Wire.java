package org.cafebabe.model.editor.workspace.circuit.component.connection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import org.cafebabe.model.IModel;
import org.cafebabe.model.editor.util.IReadOnlyMovable;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortAlreadyAddedException;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortNotConnectedException;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;

/**
 * A wire transmits logical signals from output ports to input ports.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Wire extends LogicStateContainer implements IModel {

    private static final int MINIMUM_WIRE_CONNECTIONS = 2;

    public final Event<Position> onStartPosMoved = new Event<>();
    public final Event<Position> onEndPosMoved = new Event<>();

    @Getter private final EmptyEvent onDestroy = new EmptyEvent();
    private final Set<InputPort> connectedInputs;
    private final Set<OutputPort> connectedOutputs;
    private final Set<LogicStateContainer> powerSources;
    private final Set<LogicStateContainer> gndSources;

    @Getter private IReadOnlyMovable startPos;
    @Getter private IReadOnlyMovable endPos;
    private boolean destructionPending;


    public Wire() {
        this.connectedInputs = new HashSet<>();
        this.connectedOutputs = new HashSet<>();
        this.powerSources = new HashSet<>();
        this.gndSources = new HashSet<>();
    }

    /* Public */

    /**
     * Connects an InputPort if it isn't already connected,
     * otherwise throws a RuntimeException.
     */
    public void connectInputPort(InputPort input) {
        if (isInputConnected(input)) {
            throw new PortAlreadyAddedException("An InputPort can only be added once.");
        }
        this.connectedInputs.add(input);
        input.getOnWillBeDestroyed().addListenerWithOwner(
                this::onConnectedInputPortDestroyed, this);
        input.setStateSource(this);
        setEndPos(input.getPositionTracker());
    }

    /**
     * Connects an OutputPort if it isn't already connected,
     * otherwise throws a RuntimeException.
     */
    public void connectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if (isOutputConnected(output)) {
                throw new PortAlreadyAddedException("An OutputPort can only be added once.");
            }

            this.connectedOutputs.add(output);
            output.setConnected(true);
            if (output.isHigh()) {
                this.powerSources.add(output);
            }
            if (output.isLow()) {
                this.gndSources.add(output);
            }

            output.getOnWillBeDestroyed().addListenerWithOwner(
                    this::onConnectedOutputPortDestroyed, this);

            output.getOnStateChanged().addListenerWithOwner(
                    this::onConnectedOutputStateChanged, this);

            setStartPos(output.getPositionTracker());
        });
    }

    /**
     * Disconnects an InputPort if it is connected,
     * otherwise throws a RuntimeException.
     */
    public void disconnectInputPort(InputPort input) {
        if (!isInputConnected(input)) {
            throw new PortNotConnectedException("An InputPort that isn't "
                    + "connected can't be removed.");
        }
        input.onStateChanged.removeListenersWithOwner(this);
        input.getOnWillBeDestroyed().removeListenersWithOwner(this);
        input.removeStateSource();
        this.connectedInputs.remove(input);
    }

    /**
     * Disconnects an OutputPort if it is connected,
     * otherwise throws a RuntimeException.
     */
    public void disconnectOutputPort(OutputPort output) {
        notifyIfStateChanges(() -> {
            if (!isOutputConnected(output)) {
                throw new PortNotConnectedException("An OutputPort that isn't "
                        + "connected can't be removed.");
            }
            if (!output.isDestructionPending()) {
                output.getOnWillBeDestroyed().removeListenersWithOwner(this);
            }
            output.getOnStateChanged().removeListenersWithOwner(this);
            output.setConnected(false);
            this.connectedOutputs.remove(output);
            this.powerSources.remove(output);
            this.gndSources.remove(output);
        });
    }

    @Override
    public void destroy() {
        if (!this.destructionPending) {
            this.destructionPending = true;
            disconnectAll();
            this.onDestroy.notifyListeners();
        }
    }

    @Override
    public LogicState getLogicState() {
        if (this.powerSources.isEmpty() && this.gndSources.isEmpty()) {
            return LogicState.UNDEFINED;
        }
        if (this.powerSources.isEmpty()) {
            return LogicState.LOW;
        }
        return LogicState.HIGH;
    }

    public boolean isAnyInputConnected() {
        return !this.connectedInputs.isEmpty();
    }

    public boolean isAnyOutputConnected() {
        return !this.connectedOutputs.isEmpty();
    }

    public Set<InputPort> getConnectedInputs() {
        return Collections.unmodifiableSet(this.connectedInputs);
    }

    public Set<OutputPort> getConnectedOutputs() {
        return Collections.unmodifiableSet(this.connectedOutputs);
    }

    public void disconnectAll() {
        Set<InputPort> inports = new HashSet<>(this.connectedInputs);
        for (InputPort inport : inports) {
            disconnectInputPort(inport);
        }

        Set<OutputPort> outports = new HashSet<>(this.connectedOutputs);
        for (OutputPort outport : outports) {
            disconnectOutputPort(outport);
        }
        notifyStateChange();
    }

    /**
     * Updates the wire's logical value based on updated output value.
     */
    private void onConnectedOutputStateChanged(LogicStateContainer updatedPort) {
        if (!this.connectedOutputs.contains(updatedPort)) {
            throw new PortNotConnectedException("Updated port isn't connected!");
        }
        notifyIfStateChanges(() -> {
            switch (updatedPort.getLogicState()) {
                case LOW:
                    this.gndSources.add(updatedPort);
                    this.powerSources.remove(updatedPort);
                    break;
                case HIGH:
                    this.gndSources.remove(updatedPort);
                    this.powerSources.add(updatedPort);
                    break;
                case UNDEFINED:
                    this.gndSources.remove(updatedPort);
                    this.powerSources.remove(updatedPort);
                    break;
                default:
            }
        });
    }

    private boolean isInputConnected(InputPort input) {
        return this.connectedInputs.contains(input);
    }

    private boolean isOutputConnected(OutputPort output) {
        return this.connectedOutputs.contains(output);
    }

    private int connectedPortsCount() {
        return this.connectedInputs.size() + this.connectedOutputs.size();
    }

    private void setStartPos(IReadOnlyMovable startPos) {
        if (this.startPos != null) {
            this.startPos.removePositionListeners();
        }
        this.startPos = startPos;
        this.startPos.addPositionListener(this.onStartPosMoved::notifyListeners);
        this.onStartPosMoved.notifyListeners(
                new Position(startPos.getX(), startPos.getY())
        );
    }

    private void setEndPos(IReadOnlyMovable endPos) {
        if (this.endPos != null) {
            this.endPos.removePositionListeners();
        }
        this.endPos = endPos;
        this.endPos.addPositionListener(this.onEndPosMoved::notifyListeners);
        this.onEndPosMoved.notifyListeners(
                new Position(endPos.getX(), endPos.getY()));
    }


    private void onConnectedInputPortDestroyed(InputPort port) {
        if (isInputConnected(port)) {
            disconnectInputPort(port);
        }
        if (connectedPortsCount() < MINIMUM_WIRE_CONNECTIONS) {
            destroy();
        }
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
