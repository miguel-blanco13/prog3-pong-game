package co.edu.uptc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GlobalConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static GlobalConfig instance;
    private final Properties props = new Properties();

    private GlobalConfig() {
        loadFromClasspath();
    }

    public static GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    private void loadFromClasspath() {
        try (InputStream in = getClass().getClassLoader()
                                        .getResourceAsStream(CONFIG_FILE)) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + CONFIG_FILE, e);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
