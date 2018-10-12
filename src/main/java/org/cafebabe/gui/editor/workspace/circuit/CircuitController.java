package org.cafebabe.gui.editor.workspace.circuit;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.cafebabe.gui.IController;
import org.cafebabe.gui.editor.workspace.circuit.component.ComponentController;
import org.cafebabe.gui.editor.workspace.circuit.wire.WireController;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.Wire;
import org.cafebabe.viewmodel.ISelectable;
import org.cafebabe.viewmodel.ViewModel;


/**
 * Provides a visual representation of the circuit currently being worked on.
 */
public class CircuitController implements IController {

    private final CircuitView view = new CircuitView();
    private final ViewModel viewModel;
    private final ComponentDragDropHandler componentDragDropHandler;
    private final List<ComponentController> componentControllers = new ArrayList<>();


    public CircuitController(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.componentDragDropHandler = new ComponentDragDropHandler(this.viewModel);

        this.viewModel.onComponentAdded.addListener(this::addComponent);
        this.viewModel.onWireAdded.addListener(this::addWire);
        this.viewModel.onDragSelectionDetected.addListener(this.view::addToComponentPane);
        this.viewModel.onDragSelectionReleased.addListener(this::makeDragSelection);

        this.view.onDragEnter.addListener(this.componentDragDropHandler::onComponentPaneDragEnter);
        this.view.onDragExit.addListener(this.componentDragDropHandler::onComponentPaneDragExit);
        this.view.onDragDrop.addListener(this.componentDragDropHandler::onComponentPaneDragDropped);
        this.view.onDragOver.addListener(this.componentDragDropHandler::onComponentPaneDragOver);

        this.view.onMouseDrag.addListener(this.viewModel::handleMouseDragged);
        this.view.onMouseDragReleased.addListener(this.viewModel::handleMouseDragReleased);
        this.view.onMouseMoved.addListener(this.viewModel::handleMouseMoved);
        this.view.onScroll.addListener(this.viewModel::handleScrollEvent);

        this.view.onHandleMousePress.addListener((e) -> handleMousePress());
        this.view.onHandleKeyPress.addListener(this::handleKeyPress);
    }

    /* Private */
    private void addWire(Wire wire) {
        WireController wireController = new WireController(wire);
        wireController.addClickListener(event -> {
            this.viewModel.handleControllerClick(wireController, event);
        });

        this.viewModel.addTransformable(wireController);
        this.view.addToComponentPane(wireController.getView());

        wireController.getView().toBack();
    }

    private void addComponent(Component component) {
        ComponentController newCompController = new ComponentController(component, this.viewModel);
        newCompController.getView().setOnDragDetected(event ->
                this.componentDragDropHandler.onComponentDragDetected(newCompController, event)
        );
        newCompController.addClickListener(event ->
                this.viewModel.handleControllerClick(newCompController, event)
        );
        this.componentControllers.add(newCompController);

        this.viewModel.addTransformable(newCompController);
        this.view.addToComponentPane(newCompController.getView());
    }

    private void makeDragSelection(Node selectionBox) {
        Bounds selectionBounds = selectionBox.getBoundsInParent();
        List<ISelectable> selectedComponents = getComponentsInBounds(selectionBounds);

        this.viewModel.selectComponents(selectedComponents);
        this.view.removeFromComponentPane(selectionBox);
    }

    private List<ISelectable> getComponentsInBounds(Bounds bounds) {
        List<ISelectable> componentsInBounds = new ArrayList<>();

        this.componentControllers.forEach(componentController -> {
            Bounds compBounds = componentController.getView().getBoundsInParent();
            if (bounds.intersects(compBounds)) {
                componentsInBounds.add(componentController);
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

    /* Public */
    @Override
    public Node getView() {
        return this.view;
    }

    @Override
    public void destroy() {
        this.componentControllers.forEach(IController::destroy);
        this.view.destroy();
    }
}
