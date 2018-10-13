package org.cafebabe.model.editor.workspace.circuit.simulation;

import java.util.concurrent.ThreadFactory;

/**
 * Produces threads that die along with the main thread.
 */
class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
