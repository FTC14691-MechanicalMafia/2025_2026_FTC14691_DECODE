package org.firstinspires.ftc.teamcode.vision;

public class Pose {

    private final Double x;
    private final Double y;
    private final Double z;

    public Pose(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }
}
