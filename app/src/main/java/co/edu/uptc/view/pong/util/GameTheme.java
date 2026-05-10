package co.edu.uptc.view.pong.util;

import java.awt.Color;
import java.awt.Font;

public final class GameTheme {

    private GameTheme() {}

    public static final Color BG_DARK      = Color.decode("#060D18");
    public static final Color BG_LIGHT     = Color.decode("#E8F0F8");
    public static final Color ACCENT_NEON  = Color.decode("#3B9EFF");
    public static final Color WALL_DANGER  = Color.decode("#FF4757");
    public static final Color PADDLE_DARK  = Color.WHITE;
    public static final Color PADDLE_LIGHT = Color.decode("#1A2A4A");
    public static final Color BALL_COLOR   = Color.decode("#3B9EFF");
    public static final Color BORDER_COLOR = Color.decode("#1E3A5F");
    public static final Color PANEL_SIDE   = Color.decode("#0D1B2E");
    public static final Color INFO_LABEL   = Color.decode("#8BA8CC");
    public static final Color BTN_HOVER_FG = Color.WHITE;
    public static final Color SIDE_TITLE_FG = Color.WHITE;

    public static final int WALL_WIDTH = 7;

    public static final Font FONT_HUD        = new Font(Font.MONOSPACED, Font.BOLD,  18);
    public static final Font FONT_TITLE      = new Font(Font.MONOSPACED, Font.BOLD,  72);
    public static final Font FONT_SIDE_LABEL = new Font(Font.MONOSPACED, Font.PLAIN, 13);
    public static final Font FONT_SIDE_TITLE = new Font(Font.MONOSPACED, Font.BOLD,  16);
    public static final Font FONT_SIDE_VALUE = new Font(Font.MONOSPACED, Font.BOLD,  14);
}
