package org.cafebabe.model.circuit.simulation;

import java.util.concurrent.ThreadFactory;

class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
