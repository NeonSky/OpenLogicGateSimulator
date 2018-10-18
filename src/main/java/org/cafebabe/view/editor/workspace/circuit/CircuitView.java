package org.cafebabe.view.editor.workspace.circuit;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import lombok.Getter;
import org.cafebabe.controller.editor.workspace.circuit.selection.ComponentDragDropHandler;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.connection.Wire;
import org.cafebabe.model.util.Event;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;
import org.cafebabe.view.util.CanvasGridPane;
import org.cafebabe.view.util.FxmlUtil;


/**
 * Provides a visual representation of the circuit currently being worked on.
 */
public class CircuitView extends View {

    @Getter private final Event<ComponentView> onComponentAdded = new Event<>();
    @Getter private final Event<WireView> onWireAdded = new Event<>();

    @FXML private Pane backgroundPane;
    @FXML @Getter private Pane componentPane;
    @FXML private AnchorPane simulatorControlsPane;

    @Getter private final Circuit circuit;
    @Getter private final ComponentDragDropHandler componentDragDropHandler;

    @Getter private final SimulatorToggleButtonView simulatorToggleButton =
            new SimulatorToggleButtonView();
    private final List<ComponentView> componentViews = new ArrayList<>();
    private final List<WireView> wireViews = new ArrayList<>();


    public CircuitView(Circuit circuit, ComponentDragDropHandler componentDragDropHandler) {
        this.circuit = circuit;
        this.componentDragDropHandler = componentDragDropHandler;

        FxmlUtil.attachFxml(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        setupGridPane();
        setupComponentPane();
        setupSimulatorControlsPane();

        circuit.registerSimulationStateListener(this.simulatorToggleButton::updateState);
        this.circuit.onComponentAdded.addListener(this::addComponent);
        this.circuit.onWireAdded.addListener(this::addWire);
    }

    /* Public */
    @Override
    public void init() {
        this.circuit.getComponents().forEach(this::addComponent);
        this.circuit.getWires().forEach(this::addWire);
    }

    public List<ISelectable> getComponentsInBounds(Bounds bounds) {
        List<ISelectable> componentsInBounds = new ArrayList<>();

        this.componentViews.forEach(componentView -> {
            if (componentView.isIntersecting(bounds)) {
                componentsInBounds.add(componentView);
            }
        });

        return componentsInBounds;
    }

    public void highlightInputPorts() {
        this.componentViews.forEach(ComponentView::highlightInputPorts);
    }

    public void highlightOutputPorts() {
        this.componentViews.forEach(ComponentView::highlightOutputPorts);
    }

    public void unhighlightPorts() {
        this.componentViews.forEach(ComponentView::unhighlightPorts);
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
        this.simulatorControlsPane.getChildren().add(this.simulatorToggleButton);
    }

    private void addComponent(Component component) {

        ComponentView componentView = new ComponentView(
                component,
                this.componentDragDropHandler);

        this.componentViews.add(componentView);
        addSubview(this.componentPane, componentView);
        this.onComponentAdded.notifyListeners(componentView);
    }

    private void addWire(Wire wire) {
        WireView wireView = new WireView(wire);
        this.wireViews.add(wireView);
        addSubview(this.componentPane, wireView);
        wireView.toBack();
        this.onWireAdded.notifyListeners(wireView);
    }

}
