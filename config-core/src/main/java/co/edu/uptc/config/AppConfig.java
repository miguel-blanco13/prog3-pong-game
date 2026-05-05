package co.edu.uptc.config;

/**
 * Fachada tipada sobre GlobalConfig.
 *
 * ¿Por qué esta clase existe si GlobalConfig ya tiene get()?
 *
 * Sin AppConfig, cualquier parte del código haría:
 *   double speed = Double.parseDouble(GlobalConfig.getInstance().get("ball.speed"));
 *
 * Eso tiene tres problemas:
 *   1. Repites el parse en cada lugar que lo necesites.
 *   2. Si la clave cambia de "ball.speed" a "ball.velocity", buscas y reemplazas en todo el proyecto.
 *   3. Si el valor no existe, el parseDouble explota sin mensaje claro.
 *
 * Con AppConfig, el resto del código simplemente llama AppConfig.getBallSpeed()
 * y no sabe nada de Properties, claves, ni parseo.
 * Este patrón se llama "Facade" (fachada).
 */
public class AppConfig {

    private static final GlobalConfig cfg = GlobalConfig.getInstance();

    private AppConfig() {}   // clase utilitaria — no se instancia

    public static String getAppName() {
        return cfg.get("app.name", "Pong");
    }

    public static String getLanguage() {
        return cfg.get("app.language", "es");
    }

    public static double getBallSpeed() {
        return Double.parseDouble(cfg.get("ball.speed", "250"));
    }

    public static int getBallSize() {
        return Integer.parseInt(cfg.get("ball.size", "15"));
    }

    public static int getPaddleWidth() {
        return Integer.parseInt(cfg.get("paddle.width", "14"));
    }

    public static int getPaddleHeight() {
        return Integer.parseInt(cfg.get("paddle.height", "100"));
    }

    public static int getPaddleSpeed() {
        return Integer.parseInt(cfg.get("paddle.speed", "300"));
    }

    public static double getSpeedMin() {
        return Double.parseDouble(cfg.get("speed.min", "0.5"));
    }

    public static double getSpeedMax() {
        return Double.parseDouble(cfg.get("speed.max", "3.0"));
    }

    public static double getSpeedStep() {
        return Double.parseDouble(cfg.get("speed.step", "0.2"));
    }

    public static int getSpeedTimeInterval() {
        return Integer.parseInt(cfg.get("speed.time.interval", "30"));
    }
}
