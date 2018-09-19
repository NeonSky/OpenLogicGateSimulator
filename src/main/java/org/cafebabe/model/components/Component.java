package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public abstract class Component {

    protected Map<String, InputPort> TAG_TO_INPUT = Collections.unmodifiableMap(new HashMap<>());
    protected Map<String, OutputPort> TAG_TO_OUTPUT = Collections.unmodifiableMap(new HashMap<>());


    public void connectToPort(Wire wire, String portTag) {
        if(TAG_TO_INPUT.containsKey(portTag)) wire.connectInputPort(TAG_TO_INPUT.get(portTag));
        else if(TAG_TO_OUTPUT.containsKey(portTag)) wire.connectOutputPort(TAG_TO_OUTPUT.get(portTag));
        else throw new RuntimeException("This port doesn't exist on this component");
        update();
    }

    public void disconnectFromPort(Wire wire, String portTag) {
        if(TAG_TO_INPUT.containsKey(portTag)) wire.disconnectInputPort(TAG_TO_INPUT.get(portTag));
        else if(TAG_TO_OUTPUT.containsKey(portTag)) wire.disconnectOutputPort(TAG_TO_OUTPUT.get(portTag));
        else throw new RuntimeException("This port doesn't exist on this component");
        update();
    }

    public IPort getPort(String portTag) {
        if (TAG_TO_OUTPUT.containsKey(portTag)) return TAG_TO_OUTPUT.get(portTag);
        else if (TAG_TO_INPUT.containsKey(portTag)) return TAG_TO_INPUT.get(portTag);
        throw new RuntimeException("This port doesn't exist on this component");
    }

    protected abstract void update();
    public abstract String getAnsiName();
    public abstract String getDisplayName();
    public abstract String getDescription();
}
