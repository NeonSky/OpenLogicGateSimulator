package org.cafebabe.model.components.connections;

import org.cafebabe.util.Event;

public class InputPort implements IPort {

    private Event<InputPort> onStateChanged;
    private Wire wire;


    public InputPort() {
        onStateChanged = new Event<>();
    }


    /** Is notified with the new logical value of the output whenever it is changed */
    private void onWireStateChanged(Wire wire) {
        onStateChanged.notifyAll(this);
    }

    /** Connect wire to this port */
    void connectWire(Wire wire) {
        if(this.wire != null) {
            throw new RuntimeException("A wire is already connected to this input");
        }
        this.wire = wire;
        wire.onStateChangedEvent().addListener(this::onWireStateChanged);
        onStateChanged.notifyAll(this);
    }

    /** Disconnect wire from this port */
    void disconnectWire(Wire wire) {
        if(this.wire != wire) {
            throw new RuntimeException("A wire that isn't already connected can't be disconnected.");
        }
        this.wire = null;
        wire.onStateChangedEvent().removeListener(this::onWireStateChanged);
        onStateChanged.notifyAll(this);
    }

    /** Is notified with the new logical value of the input whenever it is changed */
    public Event<InputPort> onStateChangedEvent() {
        return onStateChanged;
    }

    /** Returns true IFF the input is active */
    public boolean isActive() {
        return this.isConnected() && wire.isActive();
    }

    @Override
    public boolean isConnected() {
        return wire != null;
    }


}
