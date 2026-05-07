package co.edu.uptc.view.pong;

import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.Paddle;
import co.edu.uptc.view.pong.util.GamePanelBase;
import co.edu.uptc.view.pong.util.GameTheme;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GamePanel extends GamePanelBase {

    private GameStateDto state;

    public void applyState(GameStateDto state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackground(g2);
        drawBoundaryBox(g2);
        drawBalls(g2);
        drawPaddle(g2);
        drawDangerWall(g2);
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(state.isDarkMode() ? GameTheme.BG_DARK : GameTheme.BG_LIGHT);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBoundaryBox(Graphics2D g2) {
        g2.setColor(GameTheme.BORDER_COLOR);
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void drawBalls(Graphics2D g2) {
        g2.setColor(GameTheme.BALL_COLOR);
        for (Ball b : state.getBalls()) {
            int x = (int) b.getX();
            int y = (int) b.getY();
            int s = (int) b.getSize();
            if (state.isCircleBall()) g2.fillOval(x, y, s, s);
            else                      g2.fillRect(x, y, s, s);
        }
    }

    private void drawPaddle(Graphics2D g2) {
        Paddle p = state.getPaddle();
        g2.setColor(state.isDarkMode() ? GameTheme.PADDLE_DARK : GameTheme.PADDLE_LIGHT);
        g2.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }

    private void drawDangerWall(Graphics2D g2) {
        g2.setColor(GameTheme.WALL_DANGER);
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
    }



}
