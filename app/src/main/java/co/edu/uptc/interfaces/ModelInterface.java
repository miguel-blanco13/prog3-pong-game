package co.edu.uptc.interfaces;

import co.edu.uptc.dto.GameStateDto;

public interface ModelInterface {
    void startGame();
    void resetGame();
    void pauseGame();
    void resumeGame();
    void movePaddle(int dy);
    void addBall();
    void increaseSpeed();
    void decreaseSpeed();
    void toggleDark();
    void setCircleBall(boolean circle);
    void setFieldSize(int w, int h);
    GameStateDto getState();
    boolean isDarkMode();
    boolean isCircleBall();
}