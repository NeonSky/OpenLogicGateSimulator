package org.cafebabe.gui.editor.workspace.circuit.component.port;

import javafx.scene.Node;
import org.cafebabe.gui.IController;
import org.cafebabe.model.components.connections.Port;
import org.cafebabe.viewmodel.ViewModel;

/**
 * The common logic for port gui.
 */
public abstract class PortController implements IController {

    protected final PortView view;
    protected final ViewModel viewModel;
    private final Port port;

    PortController(double x, double y, Port port, ViewModel viewModel) {
        this.view = new PortView(port, x, y);
        this.viewModel = viewModel;
        this.port = port;

        viewModel.onConnectionStateChanged().addListener(this::handleUpdatedConnectionState);
        this.view.onClicked.addListener(this::onClick);
    }

    /* Public */
    @Override
    public Node getView() {
        return this.view;
    }

    /* Protected */
    protected void updateStyleClasses(String... extraClasses) {
        boolean isCandidate =
                this.viewModel.canConnectTo(this.port) && this.viewModel.wireHasConnections();
        this.view.updateStyleClasses(isCandidate, extraClasses);
    }

    protected abstract void onClick();

    protected abstract void handleUpdatedConnectionState();

}
