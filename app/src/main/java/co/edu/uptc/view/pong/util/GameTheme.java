package co.edu.uptc.view.pong.util;

import java.awt.Color;
import java.awt.Font;

public final class GameTheme {

    private GameTheme() {}

    public static final Color BG_DARK      = Color.decode("#0A0A0A");
    public static final Color BG_LIGHT     = Color.decode("#F5F5F5");
    public static final Color ACCENT_NEON  = Color.decode("#CBFB2B");
    public static final Color WALL_DANGER  = Color.decode("#FF3B3B");
    public static final Color PADDLE_DARK  = Color.WHITE;
    public static final Color PADDLE_LIGHT = Color.decode("#1A1A1A");
    public static final Color BALL_COLOR   = Color.decode("#CBFB2B");
    public static final Color BORDER_COLOR = Color.decode("#2a2a2a");
    public static final Color PANEL_SIDE   = Color.decode("#141414");

    public static final Font FONT_HUD        = new Font(Font.MONOSPACED, Font.BOLD,  18);
    public static final Font FONT_TITLE      = new Font(Font.MONOSPACED, Font.BOLD,  72);
    public static final Font FONT_SIDE_LABEL = new Font(Font.MONOSPACED, Font.PLAIN, 13);
}
