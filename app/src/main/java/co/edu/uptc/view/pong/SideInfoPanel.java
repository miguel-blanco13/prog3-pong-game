package co.edu.uptc.view.pong;

import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.pojo.Ball;
import co.edu.uptc.util.I18n;
import co.edu.uptc.view.pong.util.GameTheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Dimension;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SideInfoPanel extends JPanel {

    private static final I18n              i18n     = I18n.getInstance();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final JLabel bouncesName  = makeName();
    private final JLabel bouncesValue = makeValue("--");
    private final JLabel startName    = makeName();
    private final JLabel startValue   = makeValue("--:--:--");
    private final JLabel elapsedName  = makeName();
    private final JLabel elapsedValue = makeValue("00:00:00");
    private final JLabel scoreName    = makeName();
    private final JLabel scoreValue   = makeValue("0");
    private final JLabel speedName    = makeName();
    private final JLabel speedValue   = makeValue("1.0x");

    public SideInfoPanel() {
        setBackground(GameTheme.PANEL_SIDE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 0));
        initNames();
        addComponents();
    }

    private void initNames() {
        bouncesName.setText(i18n.get("hud.bounces"));
        startName.setText(i18n.get("hud.start.time"));
        elapsedName.setText(i18n.get("hud.elapsed"));
        scoreName.setText(i18n.get("hud.score"));
        speedName.setText(i18n.get("hud.speed"));
    }

    private void addComponents() {
        addHeader();
        addStats();
        add(Box.createVerticalGlue());
    }

    private void addHeader() {
        add(buildTitleLabel());
        add(Box.createVerticalStrut(8));
        add(buildSeparator());
        add(Box.createVerticalStrut(12));
    }

    private void addStats() {
        add(buildRow(bouncesName,  bouncesValue));
        add(Box.createVerticalStrut(12));
        add(buildRow(startName,   startValue));
        add(Box.createVerticalStrut(12));
        add(buildRow(elapsedName, elapsedValue));
        add(Box.createVerticalStrut(12));
        add(buildRow(scoreName,   scoreValue));
        add(Box.createVerticalStrut(12));
        add(buildRow(speedName,   speedValue));
    }

    private JLabel buildTitleLabel() {
        JLabel title = new JLabel(i18n.get("side.title"));
        title.setFont(GameTheme.FONT_SIDE_TITLE);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        return title;
    }

    private JSeparator buildSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(GameTheme.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
    }

    private JPanel buildRow(JLabel name, JLabel value) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(GameTheme.PANEL_SIDE);
        row.setAlignmentX(LEFT_ALIGNMENT);
        name.setAlignmentX(LEFT_ALIGNMENT);
        value.setAlignmentX(LEFT_ALIGNMENT);
        row.add(name);
        row.add(value);
        return row;
    }

    private static JLabel makeName() {
        JLabel l = new JLabel();
        l.setFont(GameTheme.FONT_SIDE_LABEL);
        l.setForeground(GameTheme.INFO_LABEL);
        return l;
    }

    private static JLabel makeValue(String initial) {
        JLabel l = new JLabel(initial);
        l.setFont(GameTheme.FONT_SIDE_VALUE);
        l.setForeground(GameTheme.ACCENT_NEON);
        return l;
    }

    public void applyState(GameStateDto state) {
        scoreValue.setText(String.valueOf(state.getScore()));
        speedValue.setText(String.format("%.1f", state.getSpeedMultiplier()) + "x");
        startValue.setText(formatEpoch(state.getStartTimeMs()));
        elapsedValue.setText(formatElapsed(state.getElapsedMs()));
        bouncesValue.setText(buildBouncesText(state));
    }

    private String buildBouncesText(GameStateDto state) {
        String prefix = i18n.get("side.ball.prefix");
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Ball b : state.getBalls()) {
            if (sb.length() > 0) sb.append("  ");
            sb.append(prefix).append(i++).append(":").append(b.getBounceCount());
        }
        return sb.length() > 0 ? sb.toString() : "--";
    }

    private String formatEpoch(long ms) {
        if (ms == 0) return "--:--:--";
        LocalTime t = Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalTime();
        return TIME_FMT.format(t);
    }

    private String formatElapsed(long ms) {
        long secs = ms / 1000;
        long h    = secs / 3600;
        long m    = (secs % 3600) / 60;
        long s    = secs % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
