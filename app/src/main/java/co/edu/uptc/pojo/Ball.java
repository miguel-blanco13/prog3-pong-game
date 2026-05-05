package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Ball {
    private double x, y;
    private double dx, dy;
    private double size;
    private boolean circle;
    private int bounceCount;
}
