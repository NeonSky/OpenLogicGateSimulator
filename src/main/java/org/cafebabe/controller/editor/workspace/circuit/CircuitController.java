package org.cafebabe.controller.editor.workspace.circuit;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.editor.workspace.circuit.component.ComponentController;
import org.cafebabe.controller.editor.workspace.circuit.wire.WireController;
import org.cafebabe.model.editor.workspace.circuit.Circuit;
import org.cafebabe.removemeplz.ISelectable;
import org.cafebabe.removemeplz.ViewModel;
import org.cafebabe.view.editor.workspace.circuit.CircuitView;
import org.cafebabe.view.editor.workspace.circuit.SimulatorToggleButtonView;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;
import org.cafebabe.view.editor.workspace.circuit.wire.WireView;
import org.cafebabe.view.util.FxmlUtil;


/**
 * Handles user interactions with the circuit view.
 */
public class CircuitController extends Controller {

    private final CircuitView view;
    private final ViewModel viewModel;

    public CircuitController(CircuitView view) {
        super(view);
        this.view = view;
        this.viewModel = view.viewModel;

        setSubviewAttachController(ComponentView.class, ComponentController.class);
        setSubviewAttachController(WireView.class, WireController.class);

        this.view.initializeComponentPane();

        setupEventListeners();
    }

    /* Private */
    @SuppressWarnings("checkstyle:linelength")
    private void setupEventListeners() {
        this.viewModel.onWireAdded.addListener(this.view::addWire);
        this.viewModel.onDragSelectionDetected.addListener(this.view::addToComponentPane);
        this.viewModel.onDragSelectionReleased.addListener(this::makeDragSelection);

        Pane componentPane = this.view.getComponentPane();

        FxmlUtil.onInputEventWithMeAsTarget(componentPane, MouseEvent.MOUSE_PRESSED, (e) -> handleMousePress());
        FxmlUtil.onMySceneLoaded(this.view, () ->
                FxmlUtil.onSceneInputEvent(this.view.getScene(), KeyEvent.KEY_PRESSED, this::handleKeyPress)
        );

        FxmlUtil.onInputEventWithMeAsTarget(componentPane, MouseEvent.MOUSE_DRAGGED, this.viewModel::handleMouseDragged);
        FxmlUtil.onInputEventWithMeAsTarget(componentPane, MouseEvent.MOUSE_RELEASED, this.viewModel::handleMouseDragReleased);
        FxmlUtil.onInputEventWithMeAsTarget(componentPane, MouseEvent.MOUSE_MOVED, this.viewModel::handleMouseMoved);
        componentPane.setOnScroll(this.viewModel::handleScrollEvent);

        SimulatorToggleButtonView simulatorToggleButton = this.view.getSimulatorToggleButton();
        Circuit circuit = this.view.getCircuit();

        FxmlUtil.onInputEventWithMeAsTarget(simulatorToggleButton, MouseEvent.MOUSE_CLICKED, (e) -> {
            e.consume();
            circuit.toggleSimulationState();
        });
    }

    private void makeDragSelection(Node selectionBox) {
        Bounds selectionBounds = selectionBox.getBoundsInParent();
        List<ISelectable> selectedComponents = getComponentsInBounds(selectionBounds);

        this.viewModel.selectComponents(selectedComponents);
        this.view.removeFromComponentPane(selectionBox);
    }

    private List<ISelectable> getComponentsInBounds(Bounds bounds) {
        List<ISelectable> componentsInBounds = new ArrayList<>();

        this.view.getComponentViews().forEach(componentView -> {
            Bounds compBounds = componentView.getBoundsInParent();
            if (bounds.intersects(compBounds)) {
                componentsInBounds.add(componentView);
            }
        });

        return componentsInBounds;
    }

    private void handleMousePress() {
        this.viewModel.abortSelections();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.viewModel.abortSelections();
        } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            this.viewModel.deleteSelectedControllers();
        }
    }
}
