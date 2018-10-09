package org.cafebabe.gui.editor.componentlist.cell;

import javafx.scene.Node;
import org.cafebabe.gui.IController;

/**
 * Represents a single cell / component, in the component list.
 * Contains a svg image of a component, and can be dragged to spawn that component.
 */
public class ComponentListCellController implements IController {

    private final ComponentListCellView view;


    public ComponentListCellController(String name, String svgContent) {
        this.view = new ComponentListCellView(name, svgContent);
    }

    @Override
    public Node getView() {
        return this.view;
    }

    public String getComponentName() {
        return this.view.getComponentName();
    }

    public double getWidth() {
        return this.view.getWidth();
    }

    public double getHeight() {
        return this.view.getHeight();
    }

}
