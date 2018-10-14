package org.cafebabe.model.editor.workspace.circuit.component.gate;

import java.util.Map;
import javafx.scene.media.AudioClip;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentConstructor;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;

/**
 * A component that plays a note
 * every time the power goes from
 * low / undefined to high.
 */
public class NoteComponent extends Component {

    private final InputPort input;
    private final AudioClip noteAudioClip;
    private boolean inputWasHigh;

    @ComponentConstructor
    public NoteComponent() {
        this.input = new InputPort();
        tagToInput = Map.ofEntries(
                Map.entry("input", this.input)
        );

        this.input.onStateChangedEvent().addListener(p -> updateOutputs());
        this.noteAudioClip = new AudioClip(
                this.getClass().getResource("/sound/note.wav").toExternalForm()
        );
    }

    /* Public */
    @Override
    public String getAnsiName() {
        return "NOTE_ANSI";
    }

    @Override
    public String getDisplayName() {
        return "NOTE";
    }

    @Override
    public String getDescription() {
        return "Plays note when input signal goes to high.";
    }

    @Override
    protected void updateOutputs() {
        if (this.input.isHigh() && !this.inputWasHigh) {
            this.noteAudioClip.play();
        }
        this.inputWasHigh = this.input.isHigh();
    }
}
