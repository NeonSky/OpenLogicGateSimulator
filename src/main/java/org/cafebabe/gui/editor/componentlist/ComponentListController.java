package org.cafebabe.gui.editor.componentlist;

import javafx.scene.Node;
import org.cafebabe.gui.IController;


/**
 * Provides a scrollable list of components that can be dragged onto the workspace.
 */
public class ComponentListController implements IController {

    private final ComponentListView view = new ComponentListView();

    @Override
    public Node getView() {
        return this.view;
    }
}
