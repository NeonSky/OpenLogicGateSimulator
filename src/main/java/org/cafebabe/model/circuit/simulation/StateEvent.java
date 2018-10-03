package org.cafebabe.model.circuit.simulation;

import org.cafebabe.util.Event;

class StateEvent {
    final Event eventToNotify;
    final Object paramToProvide;

    StateEvent(Event eventToNotify, Object paramToProvide) {
        this.eventToNotify = eventToNotify;
        this.paramToProvide = paramToProvide;
    }
}
