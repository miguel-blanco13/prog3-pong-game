package co.edu.uptc.interfaces;

import co.edu.uptc.dto.GameStateDto;

public interface ViewInterface {
    void setPresenter(PresenterInterface presenter);
    void start();
    void updateFrame(GameStateDto state);
    void showGameOver(int score);
}