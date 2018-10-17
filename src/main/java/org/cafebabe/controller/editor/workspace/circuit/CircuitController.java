package org.cafebabe.controller.editor.workspace.circuit;

import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.editor.workspace.circuit.component.ComponentController;
import org.cafebabe.controller.editor.workspace.circuit.selection.ComponentDragDropHandler;
import org.cafebabe.controller.editor.workspace.circuit.wire.WireController;
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

    public CircuitController(CircuitView view) {
        super(view);
        this.view = view;

        setSubviewAttachController(ComponentView.class, ComponentController.class);
        setSubviewAttachController(WireView.class, WireController.class);
        setupEventListeners();

        this.view.init();
    }

    /* Private */
    @SuppressWarnings("checkstyle:linelength")
    private void setupEventListeners() {
        ComponentDragDropHandler componentDragDropHandler = this.view.getComponentDragDropHandler();
        FxmlUtil.onInputEventWithMeAsTarget(this.view.getComponentPane(), DragEvent.DRAG_ENTERED, componentDragDropHandler::onComponentPaneDragEnter);
        FxmlUtil.onInputEventWithMeAsTarget(this.view.getComponentPane(), DragEvent.DRAG_EXITED, componentDragDropHandler::onComponentPaneDragExit);
        FxmlUtil.onInputEvent(this.view.getComponentPane(), DragEvent.DRAG_DROPPED, componentDragDropHandler::onComponentPaneDragDropped);
        FxmlUtil.onInputEvent(this.view.getComponentPane(), DragEvent.DRAG_OVER, componentDragDropHandler::onComponentPaneDragOver);

        SimulatorToggleButtonView simulatorToggleButton = this.view.getSimulatorToggleButton();
        FxmlUtil.onInputEventWithMeAsTarget(simulatorToggleButton, MouseEvent.MOUSE_CLICKED, (e) -> {
            e.consume();
            this.view.getCircuit().toggleSimulationState();
        });

        this.view.getComponentDragDropHandler().onAddComponent.addListener(
                this.view.getCircuit()::addComponent
        );
    }

}
