package org.cafebabe.model.storage.adapters;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;

/**
 * A helper struct for keeping component data to reduce complexity in the
 * ComponentAdapter read() method.
 */
@Getter
@Setter
public class ComponentData {
    private String identifier;
    private Position position;
    private BiMap<String, Long> inputIds;
    private BiMap<String, Long> outputIds;

    public boolean isValid() {
        return !(Strings.isNullOrEmpty(this.identifier)
                || Objects.isNull(this.position)
                || Objects.isNull(this.outputIds)
                || Objects.isNull(this.inputIds));
    }
}
