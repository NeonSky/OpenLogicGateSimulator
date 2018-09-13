package org.cafebabe.model.components;

import org.cafebabe.model.components.connections.Wire;

public class MockComponent extends Component {

    @Override
    public void connectToPort(Wire wire, String portTag) {
        throw new RuntimeException("This component has no output ports");
    }

    @Override
    public void disconnectFromPort(Wire wire, String portTag) {
        throw new RuntimeException("This component has no output ports");
    }

    @Override
    public String getAnsiName() {
        return "Buffer_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "Super Cool Mock Component";
    }

    @Override
    public String getDescription() {
        return "Delays the signal by one tick";
    }
}
