package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.Wire;

public abstract class Component {
    public abstract void update();
    public abstract void connectToPort(Wire wire, String portTag);
    public abstract void disconnectFromPort(Wire wire, String portTag);
    public abstract String getAnsiName();
    public abstract String getDisplayName();
    public abstract String getDescription();
}
