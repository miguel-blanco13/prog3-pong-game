package co.edu.uptc.pojo;

public class Ball {

    private double x, y;
    private double dx, dy;
    private double size;
    private boolean circle;
    private int bounceCount;

    public Ball() {}

    public Ball(double x, double y, double dx, double dy,
                double size, boolean circle, int bounceCount) {
        this.x           = x;
        this.y           = y;
        this.dx          = dx;
        this.dy          = dy;
        this.size        = size;
        this.circle      = circle;
        this.bounceCount = bounceCount;
    }

    public double  getX()           { return x; }
    public double  getY()           { return y; }
    public double  getDx()          { return dx; }
    public double  getDy()          { return dy; }
    public double  getSize()        { return size; }
    public boolean isCircle()       { return circle; }
    public int     getBounceCount() { return bounceCount; }

    public void setX(double x)            { this.x = x; }
    public void setY(double y)            { this.y = y; }
    public void setDx(double dx)          { this.dx = dx; }
    public void setDy(double dy)          { this.dy = dy; }
    public void setSize(double size)      { this.size = size; }
    public void setCircle(boolean circle) { this.circle = circle; }
    public void setBounceCount(int n)     { this.bounceCount = n; }
}
