package org.cafebabe.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.FlowPane;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.MockComponent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class EditorViewController implements Initializable {

    @FXML private Pane ROOT_PANE;
    @FXML private FlowPane componentFlowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDummyComponents();
    }

    private void addDummyComponents() {
        List<ComponentListCell> listCells = new ArrayList<>();

        Reflections reflections = new Reflections("org.cafebabe.model.components");

        Set<Class<? extends Component>> componentClasses =
                reflections.getSubTypesOf(Component.class);

        for(Class<? extends Component> componentClass : componentClasses) {
            Component component = null;
            try {
                component = componentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if(component == null) continue;
            listCells.add(new ComponentListCell(component.getDisplayName(), SvgUtil.getComponentSvgPath(component)));
        }

        componentFlowPane.getChildren().addAll(listCells);
    }
}
