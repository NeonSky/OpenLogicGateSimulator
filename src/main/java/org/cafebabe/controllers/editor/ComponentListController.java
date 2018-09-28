package org.cafebabe.controllers.editor;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.util.ComponentUtil;


class ComponentListController extends AnchorPane {
    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane componentListRoot;

    public ComponentListController() {
        FxmlUtil.attachFxml(this, "/view/ComponentList.fxml");
        List<Component> components = ComponentUtil.getAllComponents();
        List<ComponentListCellController> listCells = componentsToListCells(components);

        componentFlowPane.getChildren().addAll(listCells);
        FxmlUtil.scaleWithAnchorPaneParent(componentListRoot);
    }

    /* Private */
    private List<ComponentListCellController> componentsToListCells(List<Component> components) {
        List<ComponentListCellController> listCells = new ArrayList<>();

        for (Component component : components) {
            ComponentListCellController clcc = new ComponentListCellController(
                    component.getDisplayName(), SvgUtil.getComponentSvgPath(component)
            );
            listCells.add(clcc);
        }

        return listCells;
    }
}
