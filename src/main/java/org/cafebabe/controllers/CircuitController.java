package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.ComponentSelector;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.*;
import org.cafebabe.model.util.ComponentUtil;
import org.cafebabe.model.workspace.Position;
import org.cafebabe.util.Event;
import java.util.*;
import java.util.function.Consumer;

class CircuitController extends AnchorPane implements IWireConnector {

    @FXML private Pane backgroundPane;
    private CanvasGridPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;

    private Position dragStartedPosition;

    private ComponentController dragNewComponentController;
    private Set<WireController> wireSet = new HashSet<>();
    private Set<ComponentController> circuitComponentSet = new HashSet<>();
    private Event<IConnectionState> onConnectionStateChanged = new Event<>();
    private WireController wireController;
    private Wire wire;
    private ComponentSelector componentSelector = new ComponentSelector();

    CircuitController(Circuit circuit) {
        this.circuit = circuit;
        setupFXML();

        this.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                this.onSceneChanged(newScene);
            }
        });
    }

    private void onSceneChanged(Scene scene) {
        registerUserInputListeners(scene);
    }

    private void registerUserInputListeners(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
        scene.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleMouseClick(MouseEvent event) {
        if(event.getTarget() == this.componentPane) {
            this.abortSelections();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.abortSelections();
        } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            this.componentSelector.deleteSelectedComponents((comp) -> {
                this.circuit.safeRemove(comp.getModelObject());
                this.safeRemove(comp);
            });
            broadcastConnectionState();
        }
        refreshComponentPane();
    }

    private void abortSelections() {
        abortWireConnection();
        this.componentSelector.clearSelection();
    }

    private void abortWireConnection() {
        this.getCurrentWire().disconnectAll();
        wireSet.remove(this.getCurrentWireController());
        this.newCurrentWire();
        this.broadcastConnectionState();
    }

    private void setupFXML() {
        FxmlUtil.attachFXML(this, "/view/CircuitView.fxml");
        FxmlUtil.scaleWithAnchorPaneParent(this);

        gridPane = new CanvasGridPane();
        FxmlUtil.scaleWithAnchorPaneParent(gridPane);
        this.getChildren().add(gridPane);

        FxmlUtil.scaleWithAnchorPaneParent(componentPane);
        this.componentPane.setStyle("-fx-background-color: transparent");
        this.componentPane.toFront();
    }

    @FXML
    private void onComponentPaneDragEntered(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            ComponentListCellController componentListCellController = (ComponentListCellController)event.getGestureSource();

            Component newComponent = ComponentUtil.componentFactory(componentListCellController.getComponentName());

            dragNewComponentController = addComponent(newComponent, (int)event.getX(), (int)event.getY());

            event.consume();
        }
    }

    @FXML
    private void onComponentPaneDragExited(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController && dragNewComponentController != null) {
            removeComponent(dragNewComponentController);
            event.consume();
        }
    }

    @FXML
    private void onComponentPaneDragDropped(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentListCellController) {
            event.setDropCompleted(true);
            event.consume();
            dragNewComponentController = null;
        }
    }

    @FXML
    private void onComponentPaneDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof ComponentController) {
            /* Handle the event if the dragged item is a component controller instance */
            handleComponentDragOver(event);
        } else if (event.getGestureSource() instanceof ComponentListCellController) {
            /* Accept the event if the dragged item is a component list cell controller instance */
            handleComponentListCellDragOver(event);
        }

        event.consume();
    }

    private void handleComponentDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        ComponentController componentController = (ComponentController)event.getGestureSource();
        componentController.getPosition().translate((int)event.getX() - componentController.getPosition().getX() - dragStartedPosition.getX(),
                                                    (int)event.getY() - componentController.getPosition().getY() - dragStartedPosition.getY());

        refreshComponentPane();
    }

    private void handleComponentListCellDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);

        ComponentListCellController componentListCellController = (ComponentListCellController)event.getGestureSource();

        double height = dragNewComponentController.getHeight();
        double width = dragNewComponentController.getWidth();

        if (!componentListCellController.getComponentName().equals(dragNewComponentController.getComponent().getDisplayName())) {
            throw new RuntimeException("Dragged component from component list is not equal to currently dragged component");
        }

        dragNewComponentController.getPosition().move((int)(event.getX() - (width / 2)), (int)(event.getY() - (height / 2)));
        refreshComponentPane();
    }

    ComponentController addComponent(Component component, int x, int y) {
        this.circuit.addComponent(component);

        ComponentController newCompController = new ComponentController(component, x, y, this);
        newCompController.setOnDragDetected((event) -> onComponentDragDetected(newCompController, event));
        newCompController.addClickListener(event -> this.componentSelector.handleSelection(newCompController, event));
        this.circuitComponentSet.add(newCompController);

        refreshComponentPane();

        return newCompController;
    }

    private void onComponentDragDetected(ComponentController componentController, MouseEvent event) {
        Dragboard dragboard = componentController.startDragAndDrop(TransferMode.ANY);

        /* Need to add something (anything) to Dragboard, otherwise
         * the drag does not register on the target */
        ClipboardContent dummyContent = new ClipboardContent();
        dummyContent.put(DataFormat.PLAIN_TEXT, "foo");
        dragboard.setContent(dummyContent);
        dragboard.setDragView(new WritableImage(1, 1));

        dragStartedPosition = new Position((int)event.getX(), (int)event.getY());

        event.consume();
    }

    void removeComponent(ComponentController component) {
        this.circuit.removeComponent(component.getComponent());
        this.circuitComponentSet.remove(component);
        refreshComponentPane();
    }

    void removeWire(WireController wire) {
        this.circuit.removeWire(wire.getWire());
        this.wireSet.remove(wire);
        refreshComponentPane();
    }

    void safeRemove(ISelectable component) {
        if (this.wireSet.contains(component)) {
            this.wireSet.remove(component);
        }
        if (this.circuitComponentSet.contains(component)) {
            this.circuitComponentSet.remove(component);
        }
        refreshComponentPane();
    }

    private void refreshComponentPane() {
        this.componentPane.getChildren().clear();

        for (WireController wireController : this.wireSet) {
            wireController.updatePosition();
            this.componentPane.getChildren().add(wireController.getWireLine());
        }

        for (ComponentController componentController : this.circuitComponentSet) {
            this.componentPane.getChildren().add(componentController);
            componentController.setLayoutX(componentController.getPosition().getX());
            componentController.setLayoutY(componentController.getPosition().getY());
        }
    }

    @Override
    public void tryConnectWire(InPortController inPortController, InputPort inPort) {
        if(canConnectTo(inPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectInputPort(inPort);
            wireController.onWillBeDestroyed().addListener(this::onWireDestroyed);
            wireController.bindEndPointTo(new PositionTracker(inPortController::getPos));

            if(!wire.isAnyOutputConnected()) {
                wireController.bindStartPointTo(new PositionTracker(inPortController::getPos));
            }

            if(wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                this.newCurrentWire();
            }

            broadcastConnectionState();
            refreshComponentPane();
        }
    }

    @Override
    public void tryConnectWire(OutPortController outPortController, OutputPort outPort) {
        if(canConnectTo(outPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectOutputPort(outPort);
            wireController.bindStartPointTo(new PositionTracker(outPortController::getPos));
            if(!wire.isAnyInputConnected()) {
                wireController.bindEndPointTo(new PositionTracker(outPortController::getPos));
            }

            if(wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                newCurrentWire();
            }

            broadcastConnectionState();
            refreshComponentPane();
        }
    }


    private void newCurrentWire() {
        this.wire = null;
        this.wireController = null;
    }

    private void onWireDestroyed(WireController wire) {
        this.circuit.safeRemove(wire.getModelObject());
        this.safeRemove(wire);
    }

    private void broadcastConnectionState() {
        IConnectionState connectionState = this.getCurrentWire().getConnectionState();
        this.onConnectionStateChanged.notifyListeners(connectionState);
    }

    @Override
    public boolean canConnectTo(Port port) {
        if(port instanceof InputPort) {
            return !getCurrentWire().isAnyInputConnected() && !port.isConnected();
        } else if(port instanceof OutputPort){
            return !getCurrentWire().isAnyOutputConnected() && !port.isConnected();
        }
        else throw new RuntimeException("Invalid Port Type!");
    }

    @Override
    public void addConnectionStateListener(Consumer<IConnectionState> stateListener) {
        this.onConnectionStateChanged.addListener(stateListener);
    }

    @Override
    public void removeConnectionStateListener(Consumer<IConnectionState> stateListener) {
        this.onConnectionStateChanged.removeListener(stateListener);
    }

    @Override
    public boolean wireHasConnections() {
        return this.getCurrentWire().isAnyOutputConnected() || this.getCurrentWire().isAnyInputConnected();
    }

    private Wire getCurrentWire() {
        if(this.wire == null) {
            this.wire = new Wire();
            this.circuit.addWire(wire);
        }
        return this.wire;
    }
    
    private WireController getCurrentWireController() {
        if(this.wireController == null) {
            WireController newWireController = new WireController(this.getCurrentWire(), 0, 0, 0, 0);
            this.wireController = newWireController;
            this.wireSet.add(this.wireController);
            this.wireController.addClickListener(event -> this.componentSelector.handleSelection(newWireController, event));
        }
        return this.wireController;
    }

}
