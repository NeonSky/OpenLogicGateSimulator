package org.cafebabe.model.components;

import org.cafebabe.model.circuit.IBelongToModel;
import org.cafebabe.model.components.connections.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public abstract class Component implements IBelongToModel {

    protected Map<String, InputPort> TAG_TO_INPUT = Collections.unmodifiableMap(new HashMap<>());
    protected Map<String, OutputPort> TAG_TO_OUTPUT = Collections.unmodifiableMap(new HashMap<>());


    protected void setOutputState(OutputPort out, boolean state) {
        out.setState(state ? LogicState.HIGH : LogicState.LOW);
    }

    protected void setOutputState(OutputPort out, boolean state, List<InputPort> relatedInputs) {
        for (InputPort input : relatedInputs) {
            if (input.logicState() == LogicState.UNDEFINED) {
                out.setState(LogicState.UNDEFINED);
                return;
            }
        }

        setOutputState(out, state);
    }

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

    public Port getPort(String portTag) {
        if (TAG_TO_OUTPUT.containsKey(portTag)) return TAG_TO_OUTPUT.get(portTag);
        else if (TAG_TO_INPUT.containsKey(portTag)) return TAG_TO_INPUT.get(portTag);
        throw new RuntimeException("This port doesn't exist on this component");
    }

    public void destroy() {
        for (Map.Entry<String, InputPort> entry : TAG_TO_INPUT.entrySet()) {
            entry.getValue().destroy();
        }
        for (Map.Entry<String, OutputPort> entry : TAG_TO_OUTPUT.entrySet()) {
            entry.getValue().destroy();
        }
    }

    protected abstract void update();
    public abstract String getAnsiName();
    public abstract String getDisplayName();
    public abstract String getDescription();
}
