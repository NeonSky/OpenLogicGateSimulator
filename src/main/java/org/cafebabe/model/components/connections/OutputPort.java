package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

public class OutputPort implements IPort {

    private Event<OutputPort> onStateChanged;
    private boolean isActive;


    public OutputPort() {
        onStateChanged = new Event<>();
        isActive = false;
    }

    /** Sets the logical value of the output */
    public void setActive(boolean active) {
        if(this.isActive != active) {
            this.isActive = active;
            onStateChanged.notifyAll(this);
        }
    }

    /** Returns true IFF the output is active */
    public boolean isActive() {
        return isActive;
    }

    /** Is notified with the new logical value of the output whenever it is changed */
    Event<OutputPort> onStateChangedEvent() {
        return onStateChanged;
    }
}
