package co.edu.uptc.model;

import co.edu.uptc.config.AppConfig;
import co.edu.uptc.dto.GameStateDto;
import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.model.utils.BallList;
import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.Paddle;

import java.util.concurrent.locks.ReentrantLock;

public class GameEngine implements ModelInterface {

    private static final double TIMER_FPS          = 60.0;
    private static final double MAX_DT             = 0.016;
    private static final long   BALL_SLEEP_MS      = 8L;
    private static final long   RESET_WAIT_MS      = 20L;
    private static final long   MS_PER_SECOND      = 1_000L;

    private final BallList<Ball> balls = new BallList<>();
    private final ReentrantLock lock   = new ReentrantLock();
    private final Object pauseLock     = new Object();

    private Paddle paddle;
    private int score;
    private int lastAutoBumpScore;
    private volatile boolean gameOver;
    private volatile boolean paused;
    private boolean darkMode;
    private boolean circleBall = true;
    private double speedMultiplier = 1.0;
    private int fieldWidth  = 800;
    private int fieldHeight = 600;

    private long startTimeMs;
    private long pausedAtMs;
    private long totalPausedMs;
    private long lastTimeSpeedMs;

    public GameEngine() {
        initPaddle();
    }

    private void initPaddle() {
        paddle = new Paddle(
                fieldWidth  - AppConfig.getPaddleWidth(),
                fieldHeight / 2 - AppConfig.getPaddleHeight() / 2,
                AppConfig.getPaddleWidth(),
                AppConfig.getPaddleHeight()
        );
    }

    @Override
    public void startGame() {
        gameOver = false;
        resetCounters();
        startTimeMs = System.currentTimeMillis();
        lock.lock();
        try { balls.clear(); }
        finally { lock.unlock(); }
        addBall();
    }

    @Override
    public void resetGame() {
        gameOver = true;
        paused   = false;
        synchronized (pauseLock) { pauseLock.notifyAll(); }
        sleepMillis(RESET_WAIT_MS);
        lock.lock();
        try { balls.clear(); }
        finally { lock.unlock(); }
        resetCounters();
        initPaddle();
        gameOver = false;
    }

    private void resetCounters() {
        score             = 0;
        lastAutoBumpScore = 0;
        speedMultiplier   = 1.0;
        totalPausedMs     = 0;
        startTimeMs       = 0;
        pausedAtMs        = 0;
        lastTimeSpeedMs   = 0;
    }

    @Override
    public void pauseGame() {
        pausedAtMs = System.currentTimeMillis();
        paused     = true;
    }

    @Override
    public void resumeGame() {
        totalPausedMs += System.currentTimeMillis() - pausedAtMs;
        paused = false;
        synchronized (pauseLock) { pauseLock.notifyAll(); }
    }

    @Override
    public void movePaddle(int direction) {
        if (paused) return;
        int dy = (int) Math.round(AppConfig.getPaddleSpeed() / TIMER_FPS) * direction;
        paddle.setY(clampPaddleY(paddle.getY() + dy));
    }

    private int clampPaddleY(int y) {
        int min = 0;
        int max = fieldHeight - paddle.getHeight();
        if (y < min) return min;
        if (y > max) return max;
        return y;
    }

    @Override
    public void addBall() {
        Ball ball = createBall();
        lock.lock();
        try { balls.addLast(ball); }
        finally { lock.unlock(); }
        launchBallThread(ball);
    }

    private Ball createBall() {
        double speed = AppConfig.getBallSpeed();
        double size  = AppConfig.getBallSize();
        return new Ball(fieldWidth / 2.0, fieldHeight / 2.0,
                -speed, speed * 0.5, size, circleBall, 0);
    }

    private void launchBallThread(Ball ball) {
        Thread t = new Thread(() -> runBallLoop(ball));
        t.setDaemon(true);
        t.start();
    }

    private void runBallLoop(Ball ball) {
        long last = System.nanoTime();
        while (!gameOver) {
            waitIfPaused();
            long now  = System.nanoTime();
            double dt = Math.min((now - last) / 1_000_000_000.0, MAX_DT);
            last = now;
            updateSingleBall(ball, dt);
            sleepMillis(BALL_SLEEP_MS);
        }
    }

    private void waitIfPaused() {
        synchronized (pauseLock) {
            while (paused) {
                try { pauseLock.wait(); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void updateSingleBall(Ball ball, double dt) {
        lock.lock();
        try {
            moveBall(ball, dt);
            bounceWalls(ball);
            checkPaddleOrLost(ball);
            checkTimeSpeed();
        } finally {
            lock.unlock();
        }
    }

    private void moveBall(Ball ball, double dt) {
        ball.setX(ball.getX() + ball.getDx() * speedMultiplier * dt);
        ball.setY(ball.getY() + ball.getDy() * speedMultiplier * dt);
    }

    private void bounceWalls(Ball ball) {
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
        }
        if (ball.getY() + ball.getSize() >= fieldHeight) {
            ball.setY(fieldHeight - ball.getSize());
            ball.setDy(-Math.abs(ball.getDy()));
        }
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
        }
    }

    private void checkPaddleOrLost(Ball ball) {
        if (ball.getX() + ball.getSize() >= paddle.getX()) {
            if (hitsPaddle(ball)) {
                ball.setX(paddle.getX() - ball.getSize());
                ball.setDx(-Math.abs(ball.getDx()));
                ball.setBounceCount(ball.getBounceCount() + 1);
                score++;
                autoAdjustSpeed();
            } else {
                ball.setX(fieldWidth - ball.getSize());
                gameOver = true;
            }
        }
    }

    private boolean hitsPaddle(Ball ball) {
        double ballBottom   = ball.getY() + ball.getSize();
        double paddleBottom = paddle.getY() + paddle.getHeight();
        return ballBottom >= paddle.getY() && ball.getY() <= paddleBottom;
    }

    private void autoAdjustSpeed() {
        if (score - lastAutoBumpScore >= AppConfig.getBounceSpeedInterval()) {
            increaseSpeed();
            lastAutoBumpScore = score;
        }
    }

    private void checkTimeSpeed() {
        if (startTimeMs == 0 || paused) return;
        long interval = AppConfig.getSpeedTimeInterval() * MS_PER_SECOND;
        long elapsed  = computeElapsed();
        if (elapsed - lastTimeSpeedMs >= interval) {
            increaseSpeed();
            lastTimeSpeedMs = elapsed;
        }
    }

    @Override
    public void increaseSpeed() {
        double next = speedMultiplier + AppConfig.getSpeedStep();
        if (next <= AppConfig.getSpeedMax()) speedMultiplier = next;
    }

    @Override
    public void decreaseSpeed() {
        double next = speedMultiplier - AppConfig.getSpeedStep();
        if (next >= AppConfig.getSpeedMin()) speedMultiplier = next;
    }

    @Override
    public void toggleDark() { darkMode = !darkMode; }

    @Override
    public void setCircleBall(boolean circle) { circleBall = circle; }

    @Override
    public boolean isDarkMode()   { return darkMode; }

    @Override
    public boolean isCircleBall() { return circleBall; }

    @Override
    public GameStateDto getState() {
        GameStateDto dto = new GameStateDto();
        fillBalls(dto);
        fillGameInfo(dto);
        fillTimings(dto);
        return dto;
    }

    private void fillBalls(GameStateDto dto) {
        lock.lock();
        try { dto.setBalls(balls.toList()); }
        finally { lock.unlock(); }
    }

    private void fillGameInfo(GameStateDto dto) {
        dto.setPaddle(new Paddle(paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight()));
        dto.setScore(score);
        dto.setGameOver(gameOver);
        dto.setDarkMode(darkMode);
        dto.setCircleBall(circleBall);
        dto.setSpeedMultiplier(speedMultiplier);
        dto.setPaused(paused);
    }

    private void fillTimings(GameStateDto dto) {
        dto.setStartTimeMs(startTimeMs);
        dto.setElapsedMs(computeElapsed());
    }

    private long computeElapsed() {
        if (startTimeMs == 0) return 0;
        long base = paused ? pausedAtMs : System.currentTimeMillis();
        return base - startTimeMs - totalPausedMs;
    }

    private void sleepMillis(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Override
    public void setFieldSize(int width, int height) {
        this.fieldWidth  = width;
        this.fieldHeight = height;
        initPaddle();
    }
}
