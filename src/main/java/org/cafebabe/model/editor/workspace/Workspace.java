package org.cafebabe.model.editor.workspace;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.circuit.Circuit;

/**
 * Contains the circuit, should do more.
 */
public class Workspace {

    private final Circuit circuit;
    @Getter @Setter private String path;

    public Workspace() {
        this.circuit = new Circuit();
    }

    /* Public */
    public Circuit getCircuit() {
        return this.circuit;
    }

    public boolean isSaved() {
        return Strings.isNullOrEmpty(this.path);
    }

}
