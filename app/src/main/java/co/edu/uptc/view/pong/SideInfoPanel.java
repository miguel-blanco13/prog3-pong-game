package co.edu.uptc.view.pong;

import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.pojo.Ball;
import co.edu.uptc.view.pong.util.GameTheme;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class SideInfoPanel extends JPanel {

    private final JLabel scoreLabel   = new JLabel();
    private final JLabel speedLabel   = new JLabel();
    private final JLabel startLabel   = new JLabel();
    private final JLabel elapsedLabel = new JLabel();
    private final JPanel bouncesPanel = new JPanel();

    public SideInfoPanel() {
        setBackground(GameTheme.PANEL_SIDE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(180, 0));
        initLabels();
        addComponents();
    }

    private void initLabels() {
        styleLabel(scoreLabel);
        styleLabel(speedLabel);
        styleLabel(startLabel);
        styleLabel(elapsedLabel);
        bouncesPanel.setBackground(GameTheme.PANEL_SIDE);
        bouncesPanel.setLayout(new BoxLayout(bouncesPanel, BoxLayout.Y_AXIS));
    }

    private void styleLabel(JLabel label) {
        label.setFont(GameTheme.FONT_SIDE_LABEL);
        label.setForeground(GameTheme.ACCENT_NEON);
        label.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addComponents() {
        add(Box.createVerticalStrut(16));
        add(scoreLabel);
        add(Box.createVerticalStrut(8));
        add(speedLabel);
        add(Box.createVerticalStrut(8));
        add(startLabel);
        add(Box.createVerticalStrut(8));
        add(elapsedLabel);
        add(Box.createVerticalStrut(8));
        add(bouncesPanel);
    }

    public void applyState(GameStateDto state) {
        scoreLabel.setText("Score: "  + state.getScore());
        speedLabel.setText("Speed: "  + String.format("%.1f", state.getSpeedMultiplier()) + "x");
        startLabel.setText("Start: "  + formatEpoch(state.getStartTimeMs()));
        elapsedLabel.setText("Time: " + formatElapsed(state.getElapsedMs()));
        updateBounces(state);
    }

    private void updateBounces(GameStateDto state) {
        bouncesPanel.removeAll();
        int i = 1;
        for (Ball b : state.getBalls()) {
            JLabel lbl = new JLabel("Ball " + i++ + ": " + b.getBounceCount() + " bounces");
            styleLabel(lbl);
            bouncesPanel.add(lbl);
        }
        bouncesPanel.revalidate();
    }

    private String formatEpoch(long ms) {
        if (ms == 0) return "--:--:--";
        LocalTime t = Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalTime();
        return String.format("%02d:%02d:%02d", t.getHour(), t.getMinute(), t.getSecond());
    }

    private String formatElapsed(long ms) {
        long secs = ms / 1000;
        long h    = secs / 3600;
        long m    = (secs % 3600) / 60;
        long s    = secs % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
