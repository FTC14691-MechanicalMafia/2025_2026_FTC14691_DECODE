package org.firstinspires.ftc.teamcode;

public class AprilTag {

    private final int id;

    private final Pose pose;

    private final Rotation rotation;

    private final Targeting targeting;

    public AprilTag(int id, Pose pose, Rotation rotation, Targeting targeting) {
        this.id = id;
        this.pose = pose;
        this.rotation = rotation;
        this.targeting = targeting;
    }

    public int getId() {
        return id;
    }

    public Pose getPose() {
        return pose;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Targeting getTargetting() {
        return targeting;
    }
}
