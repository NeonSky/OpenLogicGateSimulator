package org.cafebabe.model.editor.workspace.circuit.component;

import java.util.List;
import org.cafebabe.model.editor.util.Event;
import org.cafebabe.model.editor.workspace.circuit.simulation.DynamicEvent;


public interface IDynamicComponent {
    boolean shouldDie();
    Event<DynamicEvent> getOnNewDynamicEvent();
    List<DynamicEvent> getInitialDynamicEvents();
}
