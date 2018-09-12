package org.cafebabe.model.components;

public abstract class Component {
    public abstract void update();
    public abstract String getAnsiName();
    public abstract String getDisplayName();
    public abstract String getDescription();
}
