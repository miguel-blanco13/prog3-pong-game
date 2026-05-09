package co.edu.uptc.dto;

import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.Paddle;

import java.util.List;

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

    public List<Ball> getBalls()           { return balls; }
    public Paddle     getPaddle()          { return paddle; }
    public int        getScore()           { return score; }
    public boolean    isGameOver()         { return gameOver; }
    public boolean    isPaused()           { return paused; }
    public boolean    isDarkMode()         { return darkMode; }
    public boolean    isCircleBall()       { return circleBall; }
    public double     getSpeedMultiplier() { return speedMultiplier; }
    public long       getStartTimeMs()     { return startTimeMs; }
    public long       getElapsedMs()       { return elapsedMs; }

    public void setBalls(List<Ball> balls)              { this.balls = balls; }
    public void setPaddle(Paddle paddle)                { this.paddle = paddle; }
    public void setScore(int score)                     { this.score = score; }
    public void setGameOver(boolean gameOver)           { this.gameOver = gameOver; }
    public void setPaused(boolean paused)               { this.paused = paused; }
    public void setDarkMode(boolean darkMode)           { this.darkMode = darkMode; }
    public void setCircleBall(boolean circleBall)       { this.circleBall = circleBall; }
    public void setSpeedMultiplier(double sm)           { this.speedMultiplier = sm; }
    public void setStartTimeMs(long startTimeMs)        { this.startTimeMs = startTimeMs; }
    public void setElapsedMs(long elapsedMs)            { this.elapsedMs = elapsedMs; }
}
