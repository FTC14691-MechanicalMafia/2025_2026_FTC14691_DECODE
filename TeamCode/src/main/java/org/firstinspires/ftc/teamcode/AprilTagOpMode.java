package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp
public class AprilTagOpMode extends OpMode {
    /**
     * This is the list of april tag ids that we care about.
     */
    public static final List<Integer> APRIL_TAG_IDS = Arrays.asList(20, 24);

    private AprilTagDriver aprilTagDriver = null;

    @Override
    public void init() {
        // TODO - April tag stuff (camera)
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
                    telemetry.addData(aprilTag.getId() + " X pose is " + aprilTag.getPose().getX(), "inches");
                    telemetry.addData(aprilTag.getId() + " Y pose is " + aprilTag.getPose().getY(), "inches");
                    telemetry.addData(aprilTag.getId() + " Z pose is " + aprilTag.getPose().getX(), "inches");
                    telemetry.addData(aprilTag.getId() + " Pitch is " + aprilTag.getRotation().getPitch(), "degrees");
                    telemetry.addData(aprilTag.getId() + " Roll is " + aprilTag.getRotation().getRoll(), "degrees");
                    telemetry.addData(aprilTag.getId() + " Yaw is " + aprilTag.getRotation().getYaw(), "degrees");
                    telemetry.addData(aprilTag.getId() + " Range is " + aprilTag.getTargetting().getRange(), "inches");
                    telemetry.addData(aprilTag.getId() + " Bearing is " + aprilTag.getTargetting().getBearing(), "degrees");
                    telemetry.addData(aprilTag.getId() + " Elevation is " + aprilTag.getTargetting().getElevation(), "inches");
                });

        // update telemetry
        telemetry.update();
    }
}
