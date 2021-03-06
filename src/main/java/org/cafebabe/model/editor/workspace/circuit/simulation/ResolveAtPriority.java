package org.cafebabe.model.editor.workspace.circuit.simulation;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Compares dynamic events by their dueTo time in increasing order.
 */
class ResolveAtPriority implements Comparator<DynamicEvent>, Serializable {
    private static final long serialVersionUID = 1234L;

    @Override
    public int compare(DynamicEvent o1, DynamicEvent o2) {
        return Long.compare(o1.getResolveAt(), o2.getResolveAt());
    }
}
