package co.edu.uptc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton que carga config.properties desde el classpath.
 *
 * ¿Por qué Singleton? Porque cargar un archivo del disco es costoso.
 * Con Singleton lo hacemos UNA sola vez y reutilizamos el resultado.
 *
 * ¿Por qué classpath y no una ruta absoluta?
 * Porque cuando el proyecto se empaqueta en un JAR, los archivos de
 * resources quedan dentro del JAR. ClassLoader sabe cómo leerlos
 * desde adentro; una ruta como "C:/archivos/config.properties" no.
 */
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
            throw new RuntimeException("No se pudo cargar: " + CONFIG_FILE, e);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
