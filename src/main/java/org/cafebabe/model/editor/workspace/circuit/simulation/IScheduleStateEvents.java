package org.cafebabe.model.editor.workspace.circuit.simulation;

import org.cafebabe.model.util.Event;

public interface IScheduleStateEvents {
    void queueEvent(Event eventToNotify, Object paramToProvide);
}
