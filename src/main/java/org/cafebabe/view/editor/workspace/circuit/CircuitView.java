package org.cafebabe.view.editor.workspace.circuit;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import org.cafebabe.controller.editor.workspace.circuit.ComponentDragDropHandler;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;
import org.cafebabe.view.util.CanvasGridPane;
import org.cafebabe.view.util.FxmlUtil;


/**
 * Provides a visual representation of the circuit currently being worked on.
 */
public class CircuitView extends View {

    @FXML private Pane backgroundPane;
    @FXML private Pane componentPane;
    @FXML private AnchorPane simulatorControlsPane;
    private SimulatorToggleButtonView simulatorToggleButton;

    public final ViewModel viewModel;

    private final Circuit circuit;
    private final List<ComponentView> componentViews = new ArrayList<>();
    private final List<WireView> wireViews = new ArrayList<>();


    @SuppressWarnings("PMD.UnusedFormalParameter")
    public CircuitView(Circuit circuit, ViewModel viewModel) {
        this.circuit = circuit;
        this.viewModel = viewModel;

        FxmlUtil.attachFxml(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        setupGridPane();
        setupComponentPane();
        setupSimulatorControlsPane();
        setupSimulationEventHandlers(circuit);
    }

    /* Public */
    public void addToComponentPane(Node node) {
        this.componentPane.getChildren().add(node);
    }

    public void removeFromComponentPane(Node node) {
        this.componentPane.getChildren().remove(node);
    }

    public void addWire(Wire wire) {
        WireView wireView = new WireView(wire, this.viewModel);
        this.wireViews.add(wireView);
        addSubview(this.componentPane, wireView);
        wireView.toBack();
    }

    public void addComponent(
            Component component,
            ComponentDragDropHandler componentDragDropHandler) {

        ComponentView componentView = new ComponentView(
                component,
                this.viewModel,
                componentDragDropHandler);

        this.componentViews.add(componentView);
        addSubview(this.componentPane, componentView);
    }

    public Pane getComponentPane() {
        return this.componentPane;
    }

    public List<ComponentView> getComponentViews() {
        return this.componentViews;
    }

    public SimulatorToggleButtonView getSimulatorToggleButton() {
        return this.simulatorToggleButton;
    }

    public Circuit getCircuit() {
        return this.circuit;
    }


    /* Private */
    private void setupGridPane() {
        CanvasGridPane gridPane = new CanvasGridPane();
        this.getChildren().add(gridPane);
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
    }

    private void setupComponentPane() {
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();
        FxmlUtil.scaleWithAnchorPaneParent(this.componentPane);
    }

    private void setupSimulatorControlsPane() {
        this.simulatorControlsPane.toFront();
        this.simulatorToggleButton = new SimulatorToggleButtonView();
        this.simulatorControlsPane.getChildren().add(this.simulatorToggleButton);
    }

    private void setupSimulationEventHandlers(Circuit circuit) {
        circuit.registerSimulationStateListener(this.simulatorToggleButton::updateState);
    }
}
