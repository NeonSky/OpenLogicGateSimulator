package org.cafebabe.view.editor.workspace.circuit;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import org.cafebabe.model.editor.workspace.circuit.simulation.SimulationState;
import org.cafebabe.model.editor.workspace.circuit.simulation.UndefinedSimulationStateException;
import org.cafebabe.view.View;
import org.cafebabe.view.util.FxmlUtil;

/**
 * A button view that provide users with visual feedback on whether or not
 * a simulation is currently running.
 */
public class SimulatorToggleButtonView extends View {

    private static final SVGPath STOP_PATH = new SVGPath();
    private static final SVGPath START_PATH = new SVGPath();

    public SimulatorToggleButtonView() {
        FxmlUtil.attachFxml(this, "/view/SimulatorToggleButtonView.fxml");
        initializeShapes();
        initializeButton();
    }

    /* Public */
    public void updateState(SimulationState newState) {
        SVGPath newStateShape;

        switch (newState) {
            case STARTED:
                newStateShape = STOP_PATH;
                break;
            case STOPPED:
                newStateShape = START_PATH;
                break;
            default:
                throw new UndefinedSimulationStateException();
        }

        this.setShape(newStateShape);
    }

    /* Private */
    private void initializeShapes() {
        START_PATH.setContent("M10 7 v30 l24 -15 z");
        STOP_PATH.setContent("M10 10 v24 h24 v-24 z");
    }

    private void initializeButton() {
        this.setCenterShape(true);
        this.setShape(START_PATH);
        AnchorPane.setLeftAnchor(this, 22.0);
    }
}
