package org.cafebabe.gui.editor.workspace;

import javafx.scene.Node;
import org.cafebabe.gui.IController;
import org.cafebabe.model.workspace.Workspace;
import org.cafebabe.viewmodel.ViewModel;

/**
 * Provides a pannable, zoomable workspace in which a circuit can be constructed.
 */
public class WorkspaceController implements IController {

    private final WorkspaceView view;


    public WorkspaceController(Workspace workspace) {
        ViewModel viewModel = new ViewModel(workspace);
        CircuitController circuitController = new CircuitController(viewModel);
        this.view = new WorkspaceView(circuitController);
    }

    @Override
    public Node getView() {
        return this.view;
    }
}
