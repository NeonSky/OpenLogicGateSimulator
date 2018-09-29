package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.List;
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

    public void select(List<ISelectable> selectables) {
        clearSelection();
        selectables.forEach(comp -> {
            this.selectedComponents.add(comp);
            comp.select();
        });
    }

    public void select(ISelectable component) {
        this.selectedComponents.add(component);
        component.select();
    }

    public void deselect(List<ISelectable> selectables) {
        selectables.forEach(comp -> {
            this.selectedComponents.remove(comp);
            comp.deselect();
        });
    }

    public void deselect(ISelectable component) {
        this.selectedComponents.remove(component);
        component.deselect();
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
}
