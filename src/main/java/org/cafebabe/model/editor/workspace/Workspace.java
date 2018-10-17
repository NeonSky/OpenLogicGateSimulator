package org.cafebabe.model.editor.workspace;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.camera.Camera;
import org.cafebabe.model.editor.workspace.circuit.Circuit;

/**
 * Wraps around the Circuit and adds features for
 * utilizing the Circuit such as editing it with the
 * connection manager, and showing a sub set of its
 * content through the Camera.
 */
public class Workspace {

    @Getter private final Camera camera;
    @Getter private final ConnectionManager connectionManager;
    @Getter private final Circuit circuit;
    @Getter @Setter private String path;

    public Workspace() {
        this.camera = new Camera();
        this.connectionManager = new ConnectionManager();
        this.circuit = new Circuit();

        this.connectionManager.onAddWire.addListener(this.circuit::addWire);
    }

    public boolean isSaved() {
        return !Strings.isNullOrEmpty(this.path);
    }

    public boolean isEmpty() {
        return this.circuit.isEmpty();
    }
}
