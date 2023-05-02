package com.ivr.queue;

import java.util.concurrent.LinkedBlockingDeque;

import com.ivr.dto.Hitlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileGeneratorQueue {

    private LinkedBlockingDeque<Hitlog> queue;
    private boolean isPolling = false;

    /**
     * Constructor
     */
    private FileGeneratorQueue() {
        queue = new LinkedBlockingDeque<>();
    }

    /**
     * Return Singleton instance
     * 
     * @return
     */
    public static FileGeneratorQueue getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Singleton holder class
     */
    private static class SingletonHolder {
        private final static FileGeneratorQueue INSTANCE = new FileGeneratorQueue();
    }

    /**
     * Add Hitlog file into queue
     * 
     * @param hitlog
     */
    public void add(Hitlog hitlog) {
        this.queue.add(hitlog);
    }

    /**
     * Poll files in queue
     */
    public void poll() {

        if (isPolling) {
            return;
        }

        isPolling = true;

        Thread pollThread = new Thread(() -> {
            while (true) {
                Hitlog hitlog;
                try {
                    hitlog = this.queue.take();
                    log.info("poll {}", hitlog.toString());
                } catch (InterruptedException e) {
                    log.error("polling interrupted: {}", e.toString());
                }
            }
        });

        pollThread.setDaemon(true);
        pollThread.start();
    }
}
