package org.cafebabe.controllers.util;

import javafx.scene.input.MouseEvent;
import org.cafebabe.controllers.IBelongToController;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ComponentSelector {

    Set<IBelongToController> selectedComponents = new HashSet<>();

    public void deleteSelectedComponents(Consumer<IBelongToController> remove) {
        this.selectedComponents.forEach((comp) -> {
            comp.disconnectFromWorkspace();
            remove.accept(comp);
        });
        selectedComponents.clear();
    }

    public void handleSelection(IBelongToController component, MouseEvent event) {
        if (event.isShiftDown()) {
            handleShiftClick(component);
        } else {
            handleNonShiftClick(component);
        }
    }

    private void handleShiftClick(IBelongToController component) {
        if (!this.selectedComponents.contains(component)) {
            this.select(component);
        } else {
            this.deselect(component);
        }
    }

    private void handleNonShiftClick(IBelongToController component) {
        this.clearSelection();
        this.select(component);
    }

    public void clearSelection() {
        selectedComponents.forEach(x -> x.deselect());
        this.selectedComponents.clear();
    }

    private void select(IBelongToController component) {
        component.select();
        this.selectedComponents.add(component);
    }

    private void deselect(IBelongToController component) {
        component.deselect();
        this.selectedComponents.remove(component);
    }
}
