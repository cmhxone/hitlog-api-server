package com.ivr.queue;

import java.util.LinkedList;
import java.util.Queue;

import com.ivr.dto.Hitlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileGeneratorQueue {

    private Queue<Hitlog> queue;

    private FileGeneratorQueue() {
        queue = new LinkedList<>();
    }

    private static class SingletonHolder {
        private final static FileGeneratorQueue INSTANCE = new FileGeneratorQueue();
    }

    public static FileGeneratorQueue getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Hitlog hitlog) {
        this.queue.add(hitlog);
    }
}
