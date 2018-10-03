package org.cafebabe.model.circuit.simulation;

import org.cafebabe.util.Event;

public interface IScheduleStateEvents {
    void queueEvent(StateEvent stateEvent);
    void queueEvent(Event eventToNotify, Object paramToProvide);
}
