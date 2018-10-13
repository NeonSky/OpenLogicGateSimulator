package org.cafebabe.controller.editor.componentlist;

import org.cafebabe.controller.Controller;
import org.cafebabe.view.editor.componentlist.ComponentListCellView;
import org.cafebabe.view.editor.componentlist.ComponentListView;


/**
 * Handles user interactions with the component list view.
 */
public class ComponentListController extends Controller {

    public ComponentListController(ComponentListView view) {
        super(view);
        setSubviewAttachController(ComponentListCellView.class, ComponentListCellController.class);
        view.init();
    }
}
