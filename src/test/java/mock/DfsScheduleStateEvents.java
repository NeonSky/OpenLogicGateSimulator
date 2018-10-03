package mock;

import org.cafebabe.model.circuit.simulation.IScheduleStateEvents;
import org.cafebabe.util.Event;

public class DfsScheduleStateEvents implements IScheduleStateEvents {
    @Override
    public void queueEvent(Event eventToNotify, Object paramToProvide) {
        eventToNotify.notifyListeners(paramToProvide);
    }
}
