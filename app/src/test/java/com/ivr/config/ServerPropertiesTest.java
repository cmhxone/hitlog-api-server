package com.ivr.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ServerPropertiesTest {

    @Test
    public void serverPropertiesTest() {
        ServerProperties properties = ServerProperties.getInstance();
        boolean b = properties.getBoolean("bool");
        int i = properties.getInt("int");
        long l = properties.getLong("long");
        String s = properties.getString("string");

        assertEquals(true, b);
        assertEquals(10, i);
        assertEquals(2_147_483_649L, l);
        assertEquals("String", s);
    }
}
