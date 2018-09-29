package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import org.cafebabe.model.circuit.Circuit;

class ControllerSelector {

    private final Set<ISelectable> selectedComponents = new HashSet<>();


    /* Public */
    public void deleteSelectedControllers(Circuit circuit) {
        for (ISelectable comp : this.selectedComponents) {
            circuit.safeRemove(comp.getModelObject());
            comp.getModelObject().destroy();
        }
        this.selectedComponents.clear();
    }

    public void handleControllerClick(ISelectable component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleClick(component);
        }
    }

    public void clearSelection() {
        this.selectedComponents.forEach(ISelectable::deselect);
        this.selectedComponents.clear();
    }

    /* Private */
    private void handleShiftClick(ISelectable component) {
        if (this.selectedComponents.contains(component)) {
            deselect(component);
        } else {
            select(component);
        }
    }

    private void handleClick(ISelectable component) {
        this.clearSelection();
        this.select(component);
    }

    private void deselect(ISelectable component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }

    private void select(ISelectable component) {
        component.select();
        this.selectedComponents.add(component);
    }
}
