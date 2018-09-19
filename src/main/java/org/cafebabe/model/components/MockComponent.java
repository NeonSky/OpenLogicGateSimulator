package org.cafebabe.model.components;

public class MockComponent extends Component {

    @ComponentConstructor
    public MockComponent(){}

    @Override
    protected void update() {}

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
