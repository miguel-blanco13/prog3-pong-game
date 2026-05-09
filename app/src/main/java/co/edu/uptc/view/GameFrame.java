package co.edu.uptc.view;

import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.util.I18n;
import co.edu.uptc.view.pong.GamePanel;
import co.edu.uptc.view.pong.SideInfoPanel;
import co.edu.uptc.view.pong.StartPanel;
import co.edu.uptc.view.pong.util.GameTheme;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

public class GameFrame extends JFrame implements ViewInterface {

    private static final String CARD_START = "START";
    private static final String CARD_GAME  = "GAME";

    private final I18n i18n = I18n.getInstance();
    private PresenterInterface presenter;
    private CardLayout         cards;
    private JPanel             cardContainer;
    private StartPanel         startPanel;
    private GamePanel          gamePanel;
    private SideInfoPanel      sidePanel;
    private JButton            pauseBtn;
    private JButton            addSpeedBtn;
    private JButton            decSpeedBtn;
    private JButton            addBallBtn;
    private JButton            darkBtn;
    private JButton            resetBtn;
    private JButton            homeBtn;
    private boolean            paused          = false;
    private boolean            darkMode        = false;
    private boolean            gameActive      = false;
    private boolean            confirmProgress = false;

    public GameFrame() {
        super();
    }

    @Override
    public void setPresenter(PresenterInterface presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start() {
        applyMacColorFix();
        System.setProperty("sun.java2d.uiScale", "1.0");
        setTitle(i18n.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        buildUi();
        setupGlobalKeys();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
        showCard(CARD_START);
    }

    private void applyMacColorFix() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    private void buildUi() {
        buildCards();
        buildSidePanel();
        buildSouthBar();
    }

    private void buildCards() {
        cards         = new CardLayout();
        cardContainer = new JPanel(cards);
        startPanel    = new StartPanel();
        startPanel.setPresenter(presenter);
        gamePanel     = new GamePanel();
        gamePanel.addComponentListener(buildResizeListener());
        cardContainer.add(startPanel, CARD_START);
        cardContainer.add(gamePanel,  CARD_GAME);
        add(cardContainer, BorderLayout.CENTER);
    }

    private void buildSidePanel() {
        sidePanel = new SideInfoPanel();
        add(sidePanel, BorderLayout.EAST);
    }

    private ComponentAdapter buildResizeListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                presenter.onFieldResize(gamePanel.getWidth() - GameTheme.WALL_WIDTH, gamePanel.getHeight());
            }
        };
    }

    private void buildSouthBar() {
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        south.setBackground(GameTheme.PANEL_SIDE);
        south.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, GameTheme.BORDER_COLOR));
        pauseBtn    = buildBtn(i18n.get("btn.pause"),     e -> togglePause());
        south.add(pauseBtn);
        south.add(buildDivider());
        addSpeedBtn = buildBtn(i18n.get("btn.add.speed"), e -> presenter.onIncreaseSpeed());
        decSpeedBtn = buildBtn(i18n.get("btn.dec.speed"), e -> presenter.onDecreaseSpeed());
        addBallBtn  = buildBtn(i18n.get("btn.add.ball"),  e -> presenter.onAddBall());
        darkBtn     = buildBtn(i18n.get("btn.dark.enable"), e -> toggleDarkMode());
        south.add(addSpeedBtn);
        south.add(decSpeedBtn);
        south.add(addBallBtn);
        south.add(darkBtn);
        south.add(buildDivider());
        resetBtn = buildBtn(i18n.get("btn.reset"), e -> doReset());
        homeBtn  = buildBtn(i18n.get("btn.home"),  e -> doGoHome());
        south.add(resetBtn);
        south.add(homeBtn);
        applyHomeState();
        add(south, BorderLayout.SOUTH);
    }

    private JSeparator buildDivider() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setForeground(GameTheme.BORDER_COLOR);
        sep.setPreferredSize(new java.awt.Dimension(1, 28));
        return sep;
    }

    private JButton buildBtn(String label, ActionListener listener) {
        JButton btn = new JButton(label);
        btn.setFont(GameTheme.FONT_SIDE_LABEL);
        btn.setBackground(GameTheme.PANEL_SIDE);
        btn.setForeground(GameTheme.ACCENT_NEON);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(buildBtnBorder(false));
        btn.addActionListener(listener);
        btn.addMouseListener(buildHoverListener(btn));
        return btn;
    }

    private Border buildBtnBorder(boolean hovered) {
        Color line = hovered ? GameTheme.ACCENT_NEON : GameTheme.BORDER_COLOR;
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(line, 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12));
    }

    private MouseAdapter buildHoverListener(JButton btn) {
        return new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) applyHoverStyle(btn, true);
            }
            @Override public void mouseExited(MouseEvent e) {
                applyHoverStyle(btn, false);
            }
        };
    }

    private void applyHoverStyle(JButton btn, boolean hovered) {
        btn.setBackground(hovered ? GameTheme.BORDER_COLOR : GameTheme.PANEL_SIDE);
        btn.setForeground(hovered ? Color.WHITE : GameTheme.ACCENT_NEON);
        btn.setBorder(buildBtnBorder(hovered));
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            presenter.onPauseGame();
            applyPausedState();
        } else {
            presenter.onResumeGame();
            applyPlayingState();
        }
        pauseBtn.setText(i18n.get(paused ? "btn.resume" : "btn.pause"));
    }

    private void doReset() {
        if (!confirmAction("confirm.reset.title", "confirm.reset.msg",
                           "confirm.reset.yes",  "confirm.reset.no")) return;
        paused = false;
        pauseBtn.setText(i18n.get("btn.pause"));
        applyPlayingState();
        presenter.onRestartGame();
    }

    private void doGoHome() {
        if (!confirmAction("confirm.home.title", "confirm.home.msg",
                           "confirm.home.yes",   "confirm.home.no")) return;
        paused     = false;
        gameActive = false;
        pauseBtn.setText(i18n.get("btn.pause"));
        presenter.onResetGame();
        applyHomeState();
        showCard(CARD_START);
    }

    private void toggleDarkMode() {
        presenter.onToggleDark();
        darkMode = !darkMode;
        startPanel.setDarkMode(darkMode);
        syncDarkButton(darkMode);
    }

    private void applyHomeState() {
        setBtnEnabled(pauseBtn,    false);
        setBtnEnabled(addSpeedBtn, false);
        setBtnEnabled(decSpeedBtn, false);
        setBtnEnabled(addBallBtn,  false);
        setBtnEnabled(resetBtn,    false);
        setBtnEnabled(homeBtn,     false);
        darkBtn.setEnabled(true);
    }

    private void applyPlayingState() {
        pauseBtn.setEnabled(true);
        addSpeedBtn.setEnabled(true);
        decSpeedBtn.setEnabled(true);
        addBallBtn.setEnabled(true);
        darkBtn.setEnabled(true);
        resetBtn.setEnabled(true);
        homeBtn.setEnabled(true);
    }

    private void applyPausedState() {
        pauseBtn.setEnabled(true);
        addSpeedBtn.setEnabled(true);
        decSpeedBtn.setEnabled(true);
        setBtnEnabled(addBallBtn, false);
        darkBtn.setEnabled(true);
        resetBtn.setEnabled(true);
        homeBtn.setEnabled(true);
    }

    private void setBtnEnabled(JButton btn, boolean enabled) {
        btn.setEnabled(enabled);
        if (!enabled) applyHoverStyle(btn, false);
    }

    private boolean confirmAction(String titleKey, String msgKey,
                                  String yesKey,   String noKey) {
        boolean wasPaused = paused;
        confirmProgress = true;
        if (!wasPaused) presenter.onPauseGame();
        String[] opts = { i18n.get(yesKey), i18n.get(noKey) };
        int choice = JOptionPane.showOptionDialog(this,
            i18n.get(msgKey), i18n.get(titleKey),
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opts, opts[1]);
        if (choice != 0 && !wasPaused) presenter.onResumeGame();
        confirmProgress = false;
        return choice == 0;
    }

    private void setupGlobalKeys() {
        JPanel root = (JPanel) getContentPane();
        bindArrowKey(root, KeyEvent.VK_UP,   true);
        bindArrowKey(root, KeyEvent.VK_DOWN, false);
        bindSpeedKey(root, '+', KeyEvent.VK_ADD,      true);
        bindSpeedKey(root, '-', KeyEvent.VK_SUBTRACT, false);
    }

    private void bindArrowKey(JPanel root, int vk, boolean up) {
        InputMap  im         = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am         = root.getActionMap();
        String    pressKey   = up ? "paddle_up_p" : "paddle_dn_p";
        String    releaseKey = up ? "paddle_up_r" : "paddle_dn_r";
        im.put(KeyStroke.getKeyStroke(vk, 0, false), pressKey);
        im.put(KeyStroke.getKeyStroke(vk, 0, true),  releaseKey);
        am.put(pressKey,   buildKeyAction(vk, true));
        am.put(releaseKey, buildKeyAction(vk, false));
    }

    private AbstractAction buildKeyAction(int vk, boolean pressed) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pressed) presenter.onKeyPressed(vk);
                else         presenter.onKeyReleased(vk);
            }
        };
    }

    private void bindSpeedKey(JPanel root, char c, int vk, boolean increase) {
        InputMap       im     = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap      am     = root.getActionMap();
        String         name   = increase ? "faster" : "slower";
        AbstractAction action = buildSpeedAction(increase);
        im.put(KeyStroke.getKeyStroke(c),     name);
        im.put(KeyStroke.getKeyStroke(vk, 0), name + "_np");
        am.put(name,         action);
        am.put(name + "_np", action);
    }

    private AbstractAction buildSpeedAction(boolean increase) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (increase) presenter.onIncreaseSpeed();
                else          presenter.onDecreaseSpeed();
            }
        };
    }

    private void showCard(String name) {
        cards.show(cardContainer, name);
    }

    @Override
    public void updateFrame(GameStateDto state) {
        SwingUtilities.invokeLater(() -> {
            if (!gameActive) activateGameButtons();
            syncPauseButton(state.isPaused());
            syncDarkButton(state.isDarkMode());
            gamePanel.applyState(state);
            sidePanel.applyState(state);
            showCard(CARD_GAME);
        });
    }

    private void activateGameButtons() {
        gameActive = true;
        applyPlayingState();
    }

    private void syncPauseButton(boolean modelPaused) {
        if (confirmProgress) return;
        if (paused != modelPaused) {
            paused = modelPaused;
            pauseBtn.setText(i18n.get(paused ? "btn.resume" : "btn.pause"));
            if (paused) applyPausedState();
            else        applyPlayingState();
        }
    }

    private void syncDarkButton(boolean isDark) {
        darkMode = isDark;
        darkBtn.setText(i18n.get(isDark ? "btn.dark.disable" : "btn.dark.enable"));
    }

    @Override
    public void showGameOver(int score) {
        SwingUtilities.invokeLater(() -> displayGameOverDialog(score));
    }

    private void displayGameOverDialog(int score) {
        String   msg  = i18n.get("gameover.score", score);
        String[] opts = { i18n.get("gameover.restart") };
        JOptionPane.showOptionDialog(this, msg, i18n.get("gameover.title"),
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, opts, opts[0]);
        paused = false;
        pauseBtn.setText(i18n.get("btn.pause"));
        applyPlayingState();
        presenter.onRestartGame();
    }
}
