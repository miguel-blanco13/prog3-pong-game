package co.edu.uptc.view.pong;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.view.pong.util.GameTheme;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class StartPanel extends JPanel {

    private PresenterInterface presenter;

    public StartPanel() {
        setLayout(new GridBagLayout());
        setBackground(GameTheme.BG_DARK);
        buildUi();
    }

    public void setPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
    }

    private void buildUi() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.gridy  = 0; add(buildTitle(),       gbc);
        gbc.gridy  = 1; add(buildStartButton(), gbc);
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("PONG");
        title.setFont(GameTheme.FONT_TITLE);
        title.setForeground(GameTheme.ACCENT_NEON);
        return title;
    }

    private JButton buildStartButton() {
        JButton btn = new JButton("JUGAR");
        btn.setFont(GameTheme.FONT_HUD);
        btn.setBackground(GameTheme.ACCENT_NEON);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> presenter.onStartGame());
        return btn;
    }

}