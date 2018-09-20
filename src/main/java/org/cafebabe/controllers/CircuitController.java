package org.cafebabe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.cafebabe.controllers.util.CanvasGridPane;
import org.cafebabe.controllers.util.FxmlUtil;
import org.cafebabe.model.circuit.Circuit;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.connections.*;
import org.cafebabe.model.util.ComponentUtil;
import org.cafebabe.model.workspace.Position;

import java.net.URL;
import java.util.*;

class CircuitController extends AnchorPane implements IWireConnector {

    @FXML private Pane backgroundPane;
    private CanvasGridPane gridPane;
    @FXML private Pane componentPane;

    private final Circuit circuit;

    private Position dragStartedPosition;

    private ComponentController dragNewComponentController;
    private Set<WireController> wireSet = new HashSet<>();
    private Set<ComponentController> circuitComponentSet = new HashSet<>();
    private WireController wireController;
    private Wire wire;
    private List<IConnectionStateListener> connectionStateListeners;

    CircuitController(Circuit circuit) {
        this.circuit = circuit;
        this.connectionStateListeners = new ArrayList<>();
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
        if(event.getTarget() == this.componentPane) this.abortWireConnection();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            abortWireConnection();
        }
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

    private void refreshComponentPane() {
        this.componentPane.getChildren().clear();

        for (ComponentController componentController : this.circuitComponentSet) {
            this.componentPane.getChildren().add(componentController);
            componentController.setLayoutX(componentController.getPosition().getX());
            componentController.setLayoutY(componentController.getPosition().getY());
        }

        for (WireController wireController : this.wireSet) {
            wireController.updatePosition();
            this.componentPane.getChildren().add(wireController.getWireLine());
            wireController.getWireLine().toBack();
        }
    }

    public void tryConnectWire(InPortController inPortController, InputPort inPort) {
        if(canConnectTo(inPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectInputPort(inPort);
            wireController.bindEndPointTo(inPortController::getPos);

            if(!wire.isAnyOutputConnected()) {
                wireController.bindStartPointTo(inPortController::getPos);
            }

            if(wire.isAnyInputConnected() && wire.isAnyOutputConnected()) {
                newCurrentWire();
            }

            broadcastConnectionState();
            refreshComponentPane();
        }
    }

    public void tryConnectWire(OutPortController outPortController, OutputPort outPort) {
        if(canConnectTo(outPort)) {
            WireController wireController = this.getCurrentWireController();
            getCurrentWire().connectOutputPort(outPort);
            wireController.bindStartPointTo(outPortController::getPos);
            if(!wire.isAnyInputConnected()) {
                wireController.bindEndPointTo(outPortController::getPos);
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

    private void broadcastConnectionState() {
        IConnectionState connectionState = this.getCurrentWire().getConnectionState();
        this.connectionStateListeners.forEach(l->l.handle(connectionState));
    }

    @Override
    public boolean canConnectTo(InputPort port) {
        return !getCurrentWire().isAnyInputConnected() && !port.isConnected();
    }

    @Override
    public boolean canConnectTo(OutputPort port) {
        return !getCurrentWire().isAnyOutputConnected() && !port.isConnected();
    }

    @Override
    public void addConnectionStateListener(IConnectionStateListener stateListener) {
        this.connectionStateListeners.add(stateListener);
    }

    @Override
    public void removeConnectionStateListener(IConnectionStateListener stateListener) {
        this.connectionStateListeners.remove(stateListener);
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
            this.wireController = new WireController(this.getCurrentWire(), 0,0,0,0);
            this.wireSet.add(this.wireController);
        }
        return this.wireController;
    }

}
