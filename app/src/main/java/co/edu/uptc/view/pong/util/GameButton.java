package co.edu.uptc.view.pong.util;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JButton {

    public GameButton(String label, ActionListener listener) {
        super(label);
        applyBaseStyle();
        addActionListener(listener);
        addMouseListener(buildHoverListener());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) applyHoverStyle(false);
    }

    public void applyHoverStyle(boolean hovered) {
        setBackground(hovered ? GameTheme.BORDER_COLOR : GameTheme.PANEL_SIDE);
        setForeground(hovered ? GameTheme.BTN_HOVER_FG : GameTheme.ACCENT_NEON);
        setBorder(buildBorder(hovered));
    }

    private void applyBaseStyle() {
        setFont(GameTheme.FONT_SIDE_LABEL);
        setBackground(GameTheme.PANEL_SIDE);
        setForeground(GameTheme.ACCENT_NEON);
        setOpaque(true);
        setFocusPainted(false);
        setBorder(buildBorder(false));
    }

    private Border buildBorder(boolean hovered) {
        Color line = hovered ? GameTheme.ACCENT_NEON : GameTheme.BORDER_COLOR;
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(line, 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12));
    }

    private MouseAdapter buildHoverListener() {
        return new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (isEnabled()) applyHoverStyle(true);
            }
            @Override public void mouseExited(MouseEvent e) {
                applyHoverStyle(false);
            }
        };
    }
}
