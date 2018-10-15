package org.cafebabe.model.editor.workspace.circuit.simulation;

/**
 * Exception thrown when application tries to set and/or use an invalid simulation state.
 */
public class UndefinedSimulationStateException extends RuntimeException {

    private static final long serialVersionUID = -7031747690273410370L;

    public UndefinedSimulationStateException() {
        super();
    }

    public UndefinedSimulationStateException(String s) {
        super(s);
    }
}
