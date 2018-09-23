package org.cafebabe.controllers.util;

import javafx.scene.input.MouseEvent;
import org.cafebabe.controllers.ISelectable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ComponentSelector {

    Set<ISelectable> selectedComponents = new HashSet<>();

    public void deleteSelectedComponents(Consumer<ISelectable> remove) {
        this.selectedComponents.forEach((comp) -> {
            comp.disconnectFromWorkspace();
            remove.accept(comp);
        });
        selectedComponents.clear();
    }

    public void handleSelection(ISelectable component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleNonShiftClick(component);
        }
    }

    private void handleShiftClick(ISelectable component) {
        if (!this.selectedComponents.contains(component)) {
            this.select(component);
        } else {
            this.deselect(component);
        }
    }

    private void handleNonShiftClick(ISelectable component) {
        this.clearSelection();
        this.select(component);
    }

    public void clearSelection() {
        selectedComponents.forEach(x -> x.deselect());
        this.selectedComponents.clear();
    }

    private void select(ISelectable component) {
        component.select();
        this.selectedComponents.add(component);
    }

    private void deselect(ISelectable component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }
}
