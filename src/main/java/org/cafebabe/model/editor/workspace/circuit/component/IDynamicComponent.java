package org.cafebabe.model.editor.workspace.circuit.component;

import java.util.List;
import org.cafebabe.model.editor.workspace.circuit.simulation.DynamicEvent;


public interface IDynamicComponent {
    boolean shouldDie();
    List<DynamicEvent> getInitialDynamicEvents();
}
