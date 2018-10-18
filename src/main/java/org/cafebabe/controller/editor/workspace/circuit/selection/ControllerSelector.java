package org.cafebabe.controller.editor.workspace.circuit.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

/**
 * A class for handling the selection of component and wires.
 */
public class ControllerSelector {

    @Getter private final Set<ISelectable> selectedComponents = new HashSet<>();

    /* Public */
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
        selectables.forEach(this::select);
    }

    public void select(ISelectable component) {
        this.selectedComponents.add(component);
        component.select();
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
