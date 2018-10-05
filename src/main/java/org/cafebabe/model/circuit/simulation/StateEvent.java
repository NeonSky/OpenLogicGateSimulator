package org.cafebabe.model.circuit.simulation;

import org.cafebabe.util.Event;


/**
 * Represents an event that needs to resolved to calculate part of the circuits' logical state.
 * These state events are resolved by the simulator.
 */
class StateEvent {
    final Event eventToNotify;
    final Object paramToProvide;

    StateEvent(Event eventToNotify, Object paramToProvide) {
        this.eventToNotify = eventToNotify;
        this.paramToProvide = paramToProvide;
    }
}
