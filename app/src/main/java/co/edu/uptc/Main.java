package co.edu.uptc;

import co.edu.uptc.config.AppConfig;

/**
 * Punto de entrada de la aplicación.
 *
 * Por ahora solo verifica que GlobalConfig y AppConfig funcionan.
 * En la Fase 9 (ensamblado final) aquí llamaremos a Runner.run().
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Pong arrancando ===");
        System.out.println("Nombre:      " + AppConfig.getAppName());
        System.out.println("Idioma:      " + AppConfig.getLanguage());
        System.out.println("Vel. pelota: " + AppConfig.getBallSpeed());
        System.out.println("Vel. paleta: " + AppConfig.getPaddleSpeed());
        System.out.println("======================");
        System.out.println("¡Config cargada correctamente! Fase 1 completa.");
    }
}
