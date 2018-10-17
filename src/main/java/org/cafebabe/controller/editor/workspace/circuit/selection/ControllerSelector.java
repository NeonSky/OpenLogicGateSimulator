package org.cafebabe.controller.editor.workspace.circuit.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseEvent;

/**
 * A class for handling the selection of component and wires.
 */
public class ControllerSelector {

    private final Set<ISelectable> selected = new HashSet<>();

    /* Public */
    public void handleControllerClick(ISelectable component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleClick(component);
        }
    }

    public void clearSelection() {
        this.selected.forEach(ISelectable::deselect);
        this.selected.clear();
    }

    public void select(List<ISelectable> selectables) {
        clearSelection();
        selectables.forEach(this::select);
    }

    public void select(ISelectable component) {
        this.selected.add(component);
        component.select();
    }

    public void deselect(ISelectable component) {
        this.selected.remove(component);
        component.deselect();
    }

    public Set<ISelectable> getSelectedComponents() {
        return this.selected;
    }

    /* Private */
    private void handleShiftClick(ISelectable component) {
        if (this.selected.contains(component)) {
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
