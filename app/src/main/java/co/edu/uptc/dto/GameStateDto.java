package co.edu.uptc.dto;

import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.Paddle;
import lombok.Data;

import java.util.List;

@Data
public class GameStateDto {
    private List<Ball> balls;
    private Paddle     paddle;
    private int        score;
    private boolean    gameOver;
    private boolean    paused;
    private boolean    darkMode;
    private boolean    circleBall;
    private double     speedMultiplier;
    private long       startTimeMs;
    private long       elapsedMs;
}
