package org.cafebabe.gui.editor.workspace.circuit.component;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;
import org.cafebabe.gui.IController;
import org.cafebabe.gui.editor.workspace.circuit.component.port.InPortController;
import org.cafebabe.gui.editor.workspace.circuit.component.port.OutPortController;
import org.cafebabe.gui.editor.workspace.circuit.component.port.PortController;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.Metadata;
import org.cafebabe.model.components.connections.InputPort;
import org.cafebabe.model.components.connections.OutputPort;
import org.cafebabe.model.util.SvgUtil;
import org.cafebabe.viewmodel.ISelectable;
import org.cafebabe.viewmodel.ITransformable;
import org.cafebabe.viewmodel.ViewModel;

/**
 * Provides a visual representation of a component with its ports.
 */
public class ComponentController implements IController, ISelectable, ITransformable {

    private final ComponentView view;
    private final List<PortController> ports = new ArrayList<>();
    private final Component component;


    public ComponentController(Component component, ViewModel viewModel) {
        this.component = component;
        this.component.onDestroyed().addListener(this::onModelDestroyed);

        this.addPortsFromMetadata(SvgUtil.getComponentMetadata(component), component, viewModel);
        this.view = new ComponentView(component, this.ports);
    }

    /* Public */
    public Component getComponent() {
        return this.component;
    }

    public void addClickListener(EventHandler<MouseEvent> listener) {
        this.view.addClickListener(listener);
    }

    @Override
    public void select() {
        this.view.setSelected(true);
        this.view.updateVisualState();
    }

    @Override
    public void deselect() {
        this.view.setSelected(false);
        this.view.updateVisualState();
    }

    @Override
    public void destroy() {
        this.component.destroy();
    }

    @Override
    public void setTransform(Transform transform) {
        this.view.setTransform(transform);
    }

    /* Private */
    private void addPortsFromMetadata(Metadata componentMetadata,
                                      Component component, ViewModel viewModel) {
        componentMetadata.inPortMetadata.forEach(m ->
                this.ports.add(new InPortController(m.x, m.y,
                        (InputPort) component.getPort(m.name), viewModel))
        );
        componentMetadata.outPortMetadata.forEach(m ->
                this.ports.add(new OutPortController(m.x, m.y,
                        (OutputPort) component.getPort(m.name), viewModel))
        );
    }

    @Override
    public Node getView() {
        return this.view;
    }

    /* Private */
    private void onModelDestroyed() {
        this.view.destroy();
    }
}
