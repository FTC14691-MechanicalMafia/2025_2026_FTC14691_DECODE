package org.firstinspires.ftc.teamcode.vision;

public class Rotation {

    private final Double roll;
    private final Double pitch;
    private final Double yaw;

    public Rotation(double roll, double pitch, double yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Double getRoll() {
        return roll;
    }

    public Double getPitch() {
        return pitch;
    }

    public Double getYaw() {
        return yaw;
    }
}
