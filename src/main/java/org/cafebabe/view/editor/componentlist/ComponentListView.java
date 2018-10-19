package org.cafebabe.view.editor.componentlist;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.cafebabe.model.editor.util.ComponentUtil;
import org.cafebabe.model.editor.util.SvgUtil;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.view.View;
import org.cafebabe.view.util.FxmlUtil;


/**
 * Provides a scrollable list of component that can be dragged onto the workspace.
 */
public class ComponentListView extends View {

    @FXML private FlowPane componentFlowPane;
    @FXML private AnchorPane componentListRoot;


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public ComponentListView() {
        FxmlUtil.attachFxml(this, "/view/ComponentList.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this.componentListRoot);
    }

    @Override
    public void init() {
        populateComponentFlowPane(ComponentUtil.getAllComponents());
    }

    /* Private */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void populateComponentFlowPane(List<Component> components) {
        components.sort(Comparator.comparing(Component::getIdentifier));
        List<View> cells = new ArrayList<>();

        for (Component component : components) {
            ComponentListCellView cellView = new ComponentListCellView(
                    component.getDisplayName(),
                    component.getIdentifier(),
                    SvgUtil.loadComponentSvg(component),
                    component.getDescription()
            );
            cells.add(cellView);
        }

        addSubviews(this.componentFlowPane, cells);
    }

}
