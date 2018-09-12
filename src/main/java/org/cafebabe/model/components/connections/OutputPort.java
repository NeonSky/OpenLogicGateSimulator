package org.cafebabe.model.components.connections;

public class OutputPort implements IPort {

    private boolean isActive;
    private Wire wire;


    public OutputPort() {
        isActive = false;
    }

    /** Sets the logical value of the output */
    public void setActive(boolean active) {
        if(!isActive && wire != null && !wire.isActive()) {
            // Update state of wire
        }
        this.isActive = active;
    }

    /** Returns true IFF the output is active */
    public boolean isActive() {
        return isActive;
    }
}
