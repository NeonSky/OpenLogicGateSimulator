package org.cafebabe.model.editor.workspace.circuit.component.connection;

import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortAlreadyAddedException;
import org.cafebabe.model.editor.workspace.circuit.component.connection.exceptions.PortNotConnectedException;
import org.cafebabe.model.editor.workspace.circuit.simulation.IScheduleStateEvents;
import org.cafebabe.model.util.Event;

/**
 * Contains a logic state.
 * This logic state may change and may affect others' logic state.
 */
public abstract class LogicStateContainer {

    @Getter final Event<LogicStateContainer> onStateChanged = new Event<>();
    @Setter private IScheduleStateEvents eventScheduler;

    public final boolean isUndefined() {
        return getLogicState() == LogicState.UNDEFINED;
    }

    public final boolean isLow() {
        return getLogicState() == LogicState.LOW;
    }

    public final boolean isHigh() {
        return getLogicState() == LogicState.HIGH;
    }

    /* Protected */
    /**
     * Run a function while checking if the state changed.
     */
    @SuppressWarnings("PMD.AvoidRethrowingException")
    protected void notifyIfStateChanges(Runnable stateMutator) {
        @SuppressWarnings("PMD.PrematureDeclaration")
        LogicState prevState = getLogicState();
        try {
            stateMutator.run();
        } catch (PortAlreadyAddedException | PortNotConnectedException e) {
            /* Rethrow certain exceptions to make code using this method testable */
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getLogicState() != prevState) {
            if (this.eventScheduler == null) {
                notifyStateChange();
            } else {
                this.eventScheduler.queueEvent(this.onStateChanged, this);
            }
        }
    }

    protected void notifyStateChange() {
        this.onStateChanged.notifyListeners(this);
    }

    protected abstract LogicState getLogicState();
}
