package org.cafebabe.view.editor.workspace;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.cafebabe.controller.editor.workspace.circuit.selection.ComponentDragDropHandler;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.util.Event;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.CircuitView;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Provides a pannable, zoomable workspace in which a circuit can be constructed.
 */
public class WorkspaceView extends View {

    @Getter private final Event<ComponentView> onComponentAdded = new Event<>();
    @Getter private final Event<WireView> onWireAdded = new Event<>();

    @FXML private AnchorPane workspaceRoot;
    @FXML private AnchorPane circuitAnchorPane;

    @Getter private final Workspace workspace;
    private CircuitView circuitView;


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public WorkspaceView(Workspace workspace) {
        this.workspace = workspace;

        FxmlUtil.attachFxml(this, "/view/WorkspaceView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this.workspaceRoot);
    }

    /* Public */
    @Override
    public void init() {
        this.circuitView = new CircuitView(
                this.workspace.getCircuit(),
                new ComponentDragDropHandler(this.workspace.getCamera())
        );
        addSubview(this.circuitAnchorPane, this.circuitView);

        this.circuitView.getOnComponentAdded().addListener(this.onComponentAdded::notifyListeners);
        this.circuitView.getOnWireAdded().addListener(this.onWireAdded::notifyListeners);
    }

    public List<ISelectable> getComponentsInBounds(Bounds bounds) {
        return this.circuitView.getComponentsInBounds(bounds);
    }

    public Pane getComponentPane() {
        return this.circuitView.getComponentPane();
    }

    public void highlightInputPorts() {
        this.circuitView.highlightInputPorts();
    }

    public void highlightOutputPorts() {
        this.circuitView.highlightOutputPorts();
    }

    public void unhighlightPorts() {
        this.circuitView.unhighlightPorts();
    }

}
