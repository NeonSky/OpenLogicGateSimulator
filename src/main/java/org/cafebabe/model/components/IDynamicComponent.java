package org.cafebabe.model.components;

import java.util.List;
import org.cafebabe.model.circuit.simulation.DynamicEvent;


public interface IDynamicComponent {
    boolean shouldDie();
    List<DynamicEvent> getInitialDynamicEvents();
}
