package co.edu.uptc.util;

import co.edu.uptc.config.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class I18n {

    private static I18n instance;
    private final Properties messages = new Properties();

    private I18n() {
        loadMessages(AppConfig.getLanguage());
    }

    public static I18n getInstance() {
        if (instance == null) instance = new I18n();
        return instance;
    }

    private void loadMessages(String lang) {
        String path = "messages_" + lang + ".properties";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
            if (in != null) messages.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            else            loadFallback();
        } catch (IOException e) {
            loadFallback();
        }
    }

    private void loadFallback() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("messages_es.properties")) {
            if (in != null) messages.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load any message bundle", e);
        }
    }

    public String get(String key) {
        return messages.getProperty(key, key);
    }

    public String get(String key, Object... args) {
        String value = get(key);
        for (int i = 0; i < args.length; i++) {
            value = value.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return value;
    }
}
