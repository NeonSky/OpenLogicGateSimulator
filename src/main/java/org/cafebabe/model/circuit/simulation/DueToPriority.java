package org.cafebabe.model.circuit.simulation;

import java.io.Serializable;
import java.util.Comparator;


@SuppressWarnings("PMD.MissingSerialVersionUID")
class DueToPriority implements Comparator<DynamicEvent>, Serializable {
    @Override
    public int compare(DynamicEvent o1, DynamicEvent o2) {
        return Long.compare(o1.dueTo, o2.dueTo);
    }
}
