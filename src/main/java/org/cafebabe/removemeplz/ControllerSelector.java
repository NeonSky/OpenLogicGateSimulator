package org.cafebabe.removemeplz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseEvent;

/**
 * A class for handling the selection of component and wires.
 */
class ControllerSelector {

    private final Set<ISelectable> selectedComponents = new HashSet<>();

    Set<ISelectable> getSelectedComponents() {
        return this.selectedComponents;
    }


    /* Package-Private */
    void handleControllerClick(ISelectable component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleClick(component);
        }
    }

    void clearSelection() {
        this.selectedComponents.forEach(ISelectable::deselect);
        this.selectedComponents.clear();
    }

    void select(List<ISelectable> selectables) {
        clearSelection();
        selectables.forEach(this::select);
    }

    void select(ISelectable component) {
        this.selectedComponents.add(component);
        component.select();
    }

    void deselect(List<ISelectable> selectables) {
        selectables.forEach(this::deselect);
    }

    void deselect(ISelectable component) {
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
