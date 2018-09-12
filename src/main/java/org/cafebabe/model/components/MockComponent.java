package org.cafebabe.model.components;

public class MockComponent extends Component {

    public void update() {}

    public String getAnsiName() {
        return "Buffer_ANSI";
    }

    public String getDisplayName() {
        return "Super Cool Mock Component";
    }

    public String getDescription() {
        return "Delays the signal by one tick";
    }
}
