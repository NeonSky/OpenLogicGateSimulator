package org.cafebabe.controllers;

import java.util.function.Consumer;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.connections.*;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;


public class WireConnector implements IWireConnector {

    final EmptyEvent onStateChanged = new EmptyEvent();
    final Event<WireController> onAddWire = new Event<>();
    private WireController wireController;
    private Wire wire;

    private Circuit model;


    WireConnector(Circuit model) {
        this.model = model;
    }

    /* Public */
    @Override
    public void tryConnectWire(InPortController inPortController, InputPort inPort) {
        if(canConnectTo(inPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectInputPort(inPort);
            wireController.onWillBeDestroyed().addListener(this::onWireDestroyed);
            wireController.bindEndPointTo(new PositionTracker(inPortController::getPos));

            if(!wire.isAnyOutputConnected()) {
                wireController.bindStartPointTo(new PositionTracker(inPortController::getPos));
            }

            if(wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                this.newCurrentWire();
            }

            broadcastConnectionState();
            onStateChanged.notifyListeners();
        }
    }

    @Override
    public void tryConnectWire(OutPortController outPortController, OutputPort outPort) {
        if(canConnectTo(outPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectOutputPort(outPort);
            wireController.bindStartPointTo(new PositionTracker(outPortController::getPos));
            if(!wire.isAnyInputConnected()) {
                wireController.bindEndPointTo(new PositionTracker(outPortController::getPos));
            }

            if(wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                newCurrentWire();
                onAddWire.notifyListeners(wireController);
            }

            broadcastConnectionState();
            onStateChanged.notifyListeners();
        }
    }

    @Override
    public void addConnectionStateListener(Consumer<IConnectionState> stateListener) {
        //todo
    }

    @Override
    public void removeConnectionStateListener(Consumer<IConnectionState> stateListener) {
        //todo
    }

    @Override
    public boolean canConnectTo(Port port) {
        if(port instanceof InputPort) {
            return !getCurrentWire().isAnyInputConnected() && !port.isConnected();
        } else if(port instanceof OutputPort){
            return !getCurrentWire().isAnyOutputConnected() && !port.isConnected();
        }
        else throw new RuntimeException("Invalid Port Type!");
    }

    @Override
    public boolean wireHasConnections() {
        return this.getCurrentWire().isAnyOutputConnected() || this.getCurrentWire().isAnyInputConnected();
    }


    /* Private */
    private Wire getCurrentWire() {
        if(this.wire == null) {
            this.wire = new Wire();
            this.model.addWire(wire);
        }
        return this.wire;
    }

    private WireController getCurrentWireController() {
        if(this.wireController == null) {
            WireController newWireController = new WireController(this.getCurrentWire(), 0, 0, 0, 0);
            this.wireController = newWireController;
        }
        return this.wireController;
    }

    private void onWireDestroyed(WireController wire) {
        this.model.safeRemove(wire.getModelObject());
    }

    private void newCurrentWire() {
        this.wire = null;
        this.wireController = null;
    }

    void broadcastConnectionState() {
        onStateChanged.notifyListeners();
    }

    void abortSelections() {
        abortWireConnection();
    }

    private void abortWireConnection() {
        this.getCurrentWire().disconnectAll();
        this.newCurrentWire();
        this.broadcastConnectionState();
    }

}
