package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.util.ComponentUtil;

import java.util.ArrayList;
import java.util.List;


class ComponentListController extends AnchorPane {
     @FXML
     private FlowPane componentFlowPane;

     @FXML
     private AnchorPane componentListRoot;

     public ComponentListController() {
         FxmlUtil.attachFXML(this, "/view/ComponentList.fxml");
         List<Component> components = ComponentUtil.getAllComponents();
         List<ComponentListCellController> listCells = componentsToListCells(components);

         componentFlowPane.getChildren().addAll(listCells);
         FxmlUtil.scaleWithAnchorPaneParent(componentListRoot);
     }

    private List<ComponentListCellController> componentsToListCells(List<Component> components) {
        List<ComponentListCellController> listCells = new ArrayList<>();
        for(Component component: components) {
            ComponentListCellController clcc = new ComponentListCellController(component.getDisplayName(), SvgUtil.getComponentSvgPath(component));
            listCells.add(clcc);

            clcc.setOnDragDetected(event -> {
                Dragboard db = clcc.startDragAndDrop(TransferMode.ANY);

                /* Need to add something (anything) to Dragboard, otherwise
                 * the drag does not register on the target */
                ClipboardContent c1 = new ClipboardContent();
                c1.put(DataFormat.PLAIN_TEXT, "foo");
                db.setContent(c1);

                event.consume();
            });
        }
        return listCells;
    }
}
