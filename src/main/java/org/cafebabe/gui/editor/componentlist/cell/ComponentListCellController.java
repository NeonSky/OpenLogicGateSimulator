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

    /* Public */
    @Override
    public void destroy() {
        this.view.destroy();
    }

    @Override
    public Node getView() {
        return this.view;
    }

    public double getWidth() {
        return this.view.getWidth();
    }

    public double getHeight() {
        return this.view.getHeight();
    }

}
