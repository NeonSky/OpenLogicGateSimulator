package org.cafebabe.model.components.connections;

public interface IConnectionState {
    boolean isConnecting();
    boolean isInportConnected();
    boolean isOutportConnected();
}
