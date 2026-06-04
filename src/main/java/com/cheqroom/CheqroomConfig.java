package com.cheqroom;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CheqroomConfig {

    private final String token;
    private final String userId;

    public CheqroomConfig() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found in resources");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        this.token = required(props, "cheqroom.token");
        this.userId = required(props, "cheqroom.userId");
    }

    private String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank() || value.startsWith("YOUR_")) {
            throw new RuntimeException("Missing config: " + key + " — fill in config.properties");
        }
        return value;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
}