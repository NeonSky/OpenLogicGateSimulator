package org.cafebabe.model.editor.workspace.circuit.component.connection;

import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.util.Event;

/**
 * A port that emits a logical logicState determined by its owner component.
 * Used as outputs for component.
 */
public class OutputPort extends Port {
    @Getter private final Event<OutputPort> onWillBeDestroyed = new Event<>();
    @Getter private LogicState logicState;
    @Getter @Setter private boolean connected;
    @Getter private boolean destructionPending;

    public OutputPort() {
        this.logicState = LogicState.UNDEFINED;
    }

    /* Public */
    public void setLogicState(LogicState state) {
        notifyIfStateChanges(() -> this.logicState = state);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.onWillBeDestroyed.notifyListeners(this);
        }
    }

}
