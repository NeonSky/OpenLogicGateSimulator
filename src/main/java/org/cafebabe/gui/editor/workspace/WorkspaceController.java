package org.cafebabe.gui.editor.workspace;

import javafx.scene.Node;
import org.cafebabe.gui.IController;
import org.cafebabe.gui.editor.workspace.circuit.CircuitController;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.viewmodel.ViewModel;

/**
 * Provides a pannable, zoomable workspace in which a circuit can be constructed.
 */
public class WorkspaceController implements IController {

    private final WorkspaceView view;
    private final CircuitController circuitController;


    public WorkspaceController(Workspace workspace) {
        ViewModel viewModel = new ViewModel(workspace);
        this.circuitController = new CircuitController(viewModel);
        this.view = new WorkspaceView(this.circuitController);
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @Override
    public void destroy() {
        this.circuitController.destroy();
        this.view.destroy();
    }
}
