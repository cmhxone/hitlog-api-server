package com.ivr.queue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

import com.ivr.config.ServerProperties;
import com.ivr.dto.Hitlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileGeneratorQueue {

    private static final ServerProperties properties = ServerProperties.getInstance();
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
                    generateFile(hitlog);
                } catch (InterruptedException e) {
                    log.error("polling interrupted: {}", e.toString());
                } catch (IOException e) {
                    log.error("generateFile failed: {}", e.toString());
                }
            }
        });

        pollThread.setDaemon(true);
        pollThread.start();
    }

    /**
     * Generate Hitlog file
     * 
     * @param hitlog
     * @throws IOException
     */
    private void generateFile(Hitlog hitlog) throws IOException {

        String hitlogOutputFile = properties.getString("hitlog.output.dir");
        hitlogOutputFile += hitlogOutputFile.endsWith("/") ? "" : "/";
        hitlogOutputFile += hitlog.getFilename();

        File file = new File(hitlogOutputFile);
        if (file.exists()) {
            return;
        }

        File dir = new File(file.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!file.createNewFile()) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(hitlog.getContent().getBytes());
        } catch (Exception e) {
        }
    }
}
