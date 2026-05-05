package co.edu.uptc.interfaces;

public interface PresenterInterface {
    void setModel(ModelInterface model);
    void onKeyPressed(int keyCode);
    void onKeyReleased(int keyCode);
    void onStartGame();
    void onResetGame();
    void onPauseGame();
    void onResumeGame();
    void onAddBall();
    void onIncreaseSpeed();
    void onDecreaseSpeed();
    void onToggleDark();
    void onSetBallShape(boolean circle);
    void onFieldResize(int w, int h);
}