package com.ivr.config;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerProperties {

    private static Configurations configs = new Configurations();
    private static Configuration config;

    /**
     * Constructor
     */
    private ServerProperties() {
        try {
            config = configs.properties(new File("server.properties"));
        } catch (Exception e) {
            log.error("{}", e.toString());
        }
    }

    /**
     * get singleton instance
     * 
     * @return
     */
    public static ServerProperties getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * get string property
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        return config.getString(key);
    }

    /**
     * get int property
     * 
     * @param key
     * @return
     */
    public int getInt(String key) {
        return config.getInt(key);
    }

    /**
     * get boolean property
     * 
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    /**
     * Singleton holder class for lazy-load
     */
    private static class SingletonHolder {
        private static final ServerProperties INSTANCE = new ServerProperties();
    }
}
