package org.cafebabe.model.storage.adapters;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import org.cafebabe.model.editor.workspace.Position;

/**
 * A helper struct for keeping component data to reduce complexity in the
 * ComponentAdapter read() method.
 */
public class ComponentData {
    @Getter @Setter private String displayName;
    @Getter @Setter private Position position;
    @Getter @Setter private BiMap<String, Integer> inputIds;
    @Getter @Setter private BiMap<String, Integer> outputIds;

    public boolean isValid() {
        return !(Strings.isNullOrEmpty(this.displayName)
                || Objects.isNull(this.position)
                || Objects.isNull(this.outputIds)
                || Objects.isNull(this.inputIds));
    }
}
