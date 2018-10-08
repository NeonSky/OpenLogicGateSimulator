package org.cafebabe.model.circuit.simulation;

import org.cafebabe.util.Event;

public interface IScheduleStateEvents {
    void queueEvent(Event eventToNotify, Object paramToProvide);
}
