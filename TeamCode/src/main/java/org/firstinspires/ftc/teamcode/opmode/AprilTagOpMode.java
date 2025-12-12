package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.firstinspires.ftc.teamcode.vision.AprilTagDriver;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp(name="April Tag Debug", group="Debugging")
public class AprilTagOpMode extends OpMode {
    /**
     * This is the list of april tag ids that we care about.
     */
    public static final List<Integer> APRIL_TAG_IDS = Arrays.asList(20, 24);

    private AprilTagDriver aprilTagDriver = null;
    RobotConstants constants = new RobotConstants();
    final double x_20 = constants.AT20_poses[0];
    final double y_20 = constants.AT20_poses[1];
    final double head_20 = constants.AT20_poses[2];
    final double x_24 = constants.AT24_poses[0];
    final double y_24 = constants.AT24_poses[1];
    final double head_24 = constants.AT24_poses[2];
    @Override
    public void init() {
        // April tag stuff (camera)
        aprilTagDriver = new AprilTagDriver(telemetry, hardwareMap);
        aprilTagDriver.initAprilTag();

        // init telemetry (display on the driver hub)
        telemetry.update();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void loop() {

        final List<AprilTag> aprilTags = aprilTagDriver.detectAprilTags();
        telemetry.addLine("AT IDs: " +
                aprilTags.stream()
                        // sort by the id values
                        .sorted(Comparator.comparing(AprilTag::getId))
                        // convert the april tag to its ID as a string
                        .map(aprilTag -> String.valueOf(aprilTag.getId()))
                        // join the ids together separated by a comma
                        .collect(Collectors.joining(","))
        );
        // print the tags we care about to telemetry
        aprilTags.stream()
                // filter the stream to the tags we want
                .filter(aprilTag -> APRIL_TAG_IDS.contains(aprilTag.getId()))
                .forEach(aprilTag -> {
                    telemetry.addLine(String.format("%s X pose is %.2f inches", aprilTag.getId(), aprilTag.getPose().getX()));
                    telemetry.addLine(String.format("%s Y pose is %.2f inches", aprilTag.getId(), aprilTag.getPose().getY()));
                    telemetry.addLine(String.format("%s Z pose is %.2f inches", aprilTag.getId(), aprilTag.getPose().getX()));
                    telemetry.addLine(String.format("%s Pitch is %.2f degrees", aprilTag.getId(), aprilTag.getRotation().getPitch()));
                    telemetry.addLine(String.format("%s Roll is %.2f degrees", aprilTag.getId(), aprilTag.getRotation().getRoll()));
                    telemetry.addLine(String.format("%s Yaw is %.2f degrees", aprilTag.getId(), aprilTag.getRotation().getYaw()));
                    telemetry.addLine(String.format("%s Range is %.2f inches",aprilTag.getId(), aprilTag.getTargetting().getRange()));
                    telemetry.addLine(String.format("%s Bearing is %.2f degrees", aprilTag.getId(), aprilTag.getTargetting().getBearing()));
                    telemetry.addLine(String.format("%s Elevation is %.2f degrees", aprilTag.getId(), aprilTag.getTargetting().getElevation()));
                });

        // update telemetry
        telemetry.update();
    }
    public Pose2d aprilLocate(AprilTag tag){
        double x_pose = 0.0;
        double y_pose = 0.0;
        double heading = 0.0;
        if(tag.getId() == 20){
            x_pose = x_20;
            y_pose = y_20;
            heading = head_20;
        } else{
            x_pose = x_24;
            y_pose = y_24;
            heading = head_24;
        }
        double angle = tag.getTargetting().getBearing() + heading;
        double distance =  tag.getTargetting().getRange() * Math.cos(tag.getTargetting().getElevation());
        return new Pose2d(distance * Math.cos(angle) + x_pose,distance * Math.cos(angle) + y_pose, angle - 180);
    }


}
