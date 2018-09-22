package org.cafebabe.controllers.util;

import org.cafebabe.controllers.IDisconnectable;
import org.cafebabe.controllers.ISelectable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComponentSelector<T extends ISelectable & IDisconnectable> {

    Set<T> selectedComponents = new HashSet<>();

    public void deleteSelectedComponents(List<Set<T>> allComponents) {
        this.selectedComponents.forEach((comp) -> {
            allComponents.forEach(set -> set.remove(comp));
            comp.disconnectFromWorkspace();
        });
        selectedComponents.clear();
    }

    public void handleSelection(T component) {
        if (!this.selectedComponents.contains(component)) {
            this.select(component);
        } else {
            this.deselect(component);
        }
    }

    public void clearSelection() {
        selectedComponents.forEach(x -> x.deselect());
        this.selectedComponents.clear();
    }

    private void select(T component) {
        component.select();
        this.selectedComponents.add(component);
    }

    private void deselect(T component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }
}
