package com.ivr.queue;

import java.util.LinkedList;
import java.util.Queue;

import com.ivr.dto.Hitlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileGeneratorQueue {

    private Queue<Hitlog> queue;

    /**
     * Constructor
     */
    private FileGeneratorQueue() {
        queue = new LinkedList<>();
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
}
