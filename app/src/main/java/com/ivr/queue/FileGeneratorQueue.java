package com.ivr.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ivr.dto.Hitlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileGeneratorQueue {

    private Queue<Hitlog> queue;
    private boolean isPolling = false;

    /**
     * Constructor
     */
    private FileGeneratorQueue() {
        queue = new ConcurrentLinkedQueue<>();
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

                if (this.queue.size() > 0) {
                    Hitlog hitlog = this.queue.poll();
                    log.info("poll {}", hitlog.toString());
                }
            }
        });

        pollThread.setDaemon(true);
        pollThread.start();
    }
}
