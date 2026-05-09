package co.edu.uptc.presenter;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GamePresenter  implements PresenterInterface {

    private ModelInterface model;
    private ViewInterface view;
    private Timer timer;
    private boolean upPressed;
    private boolean downPressed;

    @Override
    public void setView (ViewInterface view){
        this.view = view;
    }

    @Override
    public void setModel (ModelInterface model){
        this.model = model;
    }

    @Override
    public void onStartGame () {
        model.startGame();
        startTimer();
    }

    private void startTimer () {
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(16, e -> tick());
        timer.start();
    }

    @Override
    public void onResetGame() {
        stopTimer();
        model.resetGame();
    }

    @Override
    public void onRestartGame() {
        stopTimer();
        model.resetGame();
        model.startGame();
        startTimer();
    }

    private void stopTimer() {
        if (timer != null) timer.stop();
    }

    @Override
    public void onPauseGame () {
        model.pauseGame();
    }

    @Override
    public void onResumeGame () {
        model.resumeGame();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_UP)                          upPressed   = true;
        if (keyCode == KeyEvent.VK_DOWN)                        downPressed = true;
        if (keyCode == KeyEvent.VK_PLUS  || keyCode == KeyEvent.VK_ADD)      model.increaseSpeed();
        if (keyCode == KeyEvent.VK_MINUS || keyCode == KeyEvent.VK_SUBTRACT) model.decreaseSpeed();
    }

    @Override
    public void onKeyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_UP)   upPressed   = false;
        if (keyCode == KeyEvent.VK_DOWN) downPressed = false;
    }

    private void movePaddleIfNeeded() {
        int dy = 0;
        if (upPressed)   dy -= AppConfig.getPaddleSpeed() / 60;
        if (downPressed) dy += AppConfig.getPaddleSpeed() / 60;
        if (dy != 0) model.movePaddle(dy);
    }

    @Override
    public void onAddBall () {
        model.addBall();
    }

    @Override
    public void onIncreaseSpeed() {
        model.increaseSpeed();
    }

    @Override
    public void onDecreaseSpeed() {
        model.decreaseSpeed();
    }

    @Override
    public void onToggleDark() {
        model.toggleDark();
    }

    @Override
    public void onSetBallShape (boolean circle){
        model.setCircleBall(circle);
    }

    @Override
    public void onFieldResize(int w , int h){
        model.setFieldSize(w,h);
    }

    private void tick() {
        movePaddleIfNeeded();
        GameStateDto state = model.getState();
        view.updateFrame(state);
        if (state.isGameOver()) {
            stopTimer();
            view.showGameOver(state.getScore());
        }
    }

}