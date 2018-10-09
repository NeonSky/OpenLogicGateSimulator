package org.cafebabe.gui.editor.componentlist;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.cafebabe.gui.editor.componentlist.cell.ComponentListCellController;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.gui.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.util.ComponentUtil;


/**
 * The component list visual.
 */
class ComponentListView extends AnchorPane {
    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane componentListRoot;


    ComponentListView() {
        FxmlUtil.attachFxml(this, "/view/ComponentList.fxml");
        List<Component> components = ComponentUtil.getAllComponents();
        List<Node> listCells = componentsToListCells(components);

        this.componentFlowPane.getChildren().addAll(listCells);
        FxmlUtil.scaleWithAnchorPaneParent(this.componentListRoot);
    }

    /* Private */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private List<Node> componentsToListCells(List<Component> components) {
        List<Node> listCells = new ArrayList<>();

        for (Component component : components) {
            Node node = new ComponentListCellController(
                    component.getDisplayName(), SvgUtil.getComponentSvgPath(component)
            ).getView();
            listCells.add(node);
        }

        return listCells;
    }
}
