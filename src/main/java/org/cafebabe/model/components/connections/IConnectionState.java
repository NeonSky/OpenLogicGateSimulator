package org.cafebabe.model.components.connections;

public interface IConnectionState {
    /* Public */
    boolean isConnecting();

    boolean isInportConnected();

    boolean isOutportConnected();
}
