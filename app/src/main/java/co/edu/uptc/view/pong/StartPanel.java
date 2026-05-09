package co.edu.uptc.view.pong;

import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.util.I18n;
import co.edu.uptc.view.pong.util.GamePanelBase;
import co.edu.uptc.view.pong.util.GameTheme;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class StartPanel extends GamePanelBase {

    private final I18n   i18n = I18n.getInstance();
    private PresenterInterface presenter;
    private JRadioButton       circleBtn;
    private JRadioButton       squareBtn;
    private JLabel             shapeLabel;

    public StartPanel() {
        setOpaque(true);
        setLayout(new GridBagLayout());
        setBackground(GameTheme.BG_LIGHT);
        buildUi();
    }

    public void setPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void setDarkMode(boolean dark) {
        setBackground(dark ? GameTheme.BG_DARK : GameTheme.BG_LIGHT);
        applyTextColor(dark ? GameTheme.PADDLE_DARK : GameTheme.PADDLE_LIGHT);
        repaint();
    }

    private void applyTextColor(java.awt.Color color) {
        shapeLabel.setForeground(color);
        circleBtn.setForeground(color);
        squareBtn.setForeground(color);
        circleBtn.setBackground(getBackground());
        squareBtn.setBackground(getBackground());
    }

    private void buildUi() {
        GridBagConstraints gbc = defaultGbc();
        gbc.gridy = 0; add(buildTitle(),       gbc);
        gbc.gridy = 1; add(buildShapeLabel(),  gbc);
        gbc.gridy = 2; add(buildCircleBtn(),   gbc);
        gbc.gridy = 3; add(buildSquareBtn(),   gbc);
        gbc.gridy = 4; add(buildStartButton(), gbc);
        groupRadios();
    }

    private GridBagConstraints defaultGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 12, 10, 12);
        return gbc;
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel(i18n.get("start.title"));
        title.setFont(GameTheme.FONT_TITLE);
        title.setForeground(GameTheme.ACCENT_NEON);
        return title;
    }

    private JLabel buildShapeLabel() {
        shapeLabel = new JLabel(i18n.get("start.shape.label"));
        shapeLabel.setFont(GameTheme.FONT_HUD);
        shapeLabel.setForeground(GameTheme.PADDLE_LIGHT);
        return shapeLabel;
    }

    private JRadioButton buildCircleBtn() {
        circleBtn = buildRadio(i18n.get("start.shape.circle"), true);
        return circleBtn;
    }

    private JRadioButton buildSquareBtn() {
        squareBtn = buildRadio(i18n.get("start.shape.square"), false);
        return squareBtn;
    }

    private JRadioButton buildRadio(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(GameTheme.FONT_HUD);
        rb.setForeground(GameTheme.PADDLE_LIGHT);
        rb.setBackground(GameTheme.BG_LIGHT);
        rb.setFocusPainted(false);
        return rb;
    }

    private void groupRadios() {
        ButtonGroup g = new ButtonGroup();
        g.add(circleBtn);
        g.add(squareBtn);
    }

    private JButton buildStartButton() {
        JButton btn = new JButton(i18n.get("start.button"));
        btn.setFont(GameTheme.FONT_HUD);
        btn.setBackground(GameTheme.PANEL_SIDE);
        btn.setForeground(GameTheme.PADDLE_DARK);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> startGame());
        return btn;
    }

    private void startGame() {
        presenter.onSetBallShape(circleBtn.isSelected());
        presenter.onStartGame();
    }
}
