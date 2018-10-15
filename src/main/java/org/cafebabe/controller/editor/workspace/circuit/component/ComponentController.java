package org.cafebabe.controller.editor.workspace.circuit.component;

import javafx.scene.input.MouseEvent;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.editor.workspace.circuit.component.port.InPortController;
import org.cafebabe.controller.editor.workspace.circuit.component.port.OutPortController;
import org.cafebabe.model.editor.util.SvgUtil;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.Metadata;
import org.cafebabe.view.editor.workspace.circuit.component.ComponentView;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;

/**
 * Handles user interactions with the editor view.
 */
public class ComponentController extends Controller {

    private final ComponentView view;
    private final Component component;
    private boolean destructionPending;


    public ComponentController(ComponentView view) {
        super(view);
        this.view = view;
        this.component = view.getComponent();
        this.component.getOnDestroy().addListener(this::destroy);

        setSubviewAttachController(InPortView.class, InPortController.class);
        setSubviewAttachController(OutPortView.class, OutPortController.class);

        this.component.getTrackablePosition().addPositionListener(this.view::updatePosition);

        view.setOnDragDetected(event ->
                view.componentDragDropHandler.onComponentDragDetected(this.view, event)
        );
        this.view.getComponentSvgPath().addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                view.viewModel.handleControllerClick(this.view, event)
        );

        view.viewModel.addTransformable(this.view::setTransform);

        Metadata metadata = SvgUtil.getComponentMetadata(this.component);
        view.addPortsFromMetadata(metadata, this.component, view.viewModel);
    }

    /* Public */
    @Override
    public void destroy() {
        super.destroy();
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.component.destroy();
        }
    }
}