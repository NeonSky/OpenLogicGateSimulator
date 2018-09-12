package org.cafebabe.model.components.connections;

public class InputPort implements IPort {

    private boolean isActive;


    public InputPort() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
}
