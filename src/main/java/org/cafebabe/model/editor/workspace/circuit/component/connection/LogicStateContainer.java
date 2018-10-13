package org.cafebabe.model.editor.workspace.circuit.component.connection;

import org.cafebabe.model.editor.util.Event;
import org.cafebabe.model.editor.workspace.circuit.simulation.IScheduleStateEvents;

/**
 * Contains a logic state.
 * This logic state may change and may affect others' logic state.
 */
public abstract class LogicStateContainer {

    final Event<LogicStateContainer> onStateChanged = new Event<>();
    private IScheduleStateEvents eventScheduler;


    public void setEventScheduler(IScheduleStateEvents eventScheduler) {
        this.eventScheduler = eventScheduler;
    }

    public final boolean isUndefined() {
        return logicState() == LogicState.UNDEFINED;
    }

    public final boolean isLow() {
        return logicState() == LogicState.LOW;
    }

    public final boolean isHigh() {
        return logicState() == LogicState.HIGH;
    }

    public final Event<LogicStateContainer> onStateChangedEvent() {
        return this.onStateChanged;
    }

    /* Protected */
    /**
     * Run a function while checking if the state changed.
     */
    protected void notifyIfStateChanges(Runnable stateMutator) {
        LogicState prevState = logicState();
        try {
            stateMutator.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (logicState() != prevState) {
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

    protected abstract LogicState logicState();
}
