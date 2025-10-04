package org.firstinspires.ftc.teamcode;


import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "Concept: AprilTag", group = "Concept")
@Disabled
public class AprilTags extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        initAprilTag();
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                List<Double> aprilPose = telemetryAprilTag();

                // Push telemetry to the Driver Station.
                telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                // maybe fix the CPU issue by erasing data right after use?
                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                // Share the CPU.
                sleep(20);
            }
        }
        visionPortal.close();
    }
    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder()
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }
    private List<Double> telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        double xPose = 0;
        double yPose = 0;
        double zPose = 0;
        double pitch = 0; //y-rotation
        double roll = 0; //x-rotation
        double yaw = 0; //z-rotation
        double range = 0; //distance
        double bearing = 0; //
        double elevation = 0; //
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                xPose = detection.ftcPose.x;
                yPose = detection.ftcPose.y;
                zPose = detection.ftcPose.z;
                pitch = detection.ftcPose.pitch;
                roll = detection.ftcPose.roll;
                yaw = detection.ftcPose.yaw;
                range = detection.ftcPose.range;
                bearing = detection.ftcPose.bearing;
                elevation = detection.ftcPose.elevation;
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", xPose, yPose, zPose));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", pitch, roll, yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", range, bearing, elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
        return List.of(xPose, yPose, zPose, pitch, roll, yaw, range, bearing, elevation);
    }
}