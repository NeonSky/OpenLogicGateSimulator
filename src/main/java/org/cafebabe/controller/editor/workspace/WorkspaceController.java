package org.cafebabe.controller.editor.workspace;

import org.cafebabe.controller.Controller;
import org.cafebabe.controller.editor.workspace.circuit.CircuitController;
import org.cafebabe.view.editor.workspace.WorkspaceView;
import org.cafebabe.view.editor.workspace.circuit.CircuitView;

/**
 * Handles user interactions with the workspace view.
 */
public class WorkspaceController extends Controller {

    public WorkspaceController(WorkspaceView view) {
        super(view);
        setSubviewAttachController(CircuitView.class, CircuitController.class);
        view.init();
    }
}
