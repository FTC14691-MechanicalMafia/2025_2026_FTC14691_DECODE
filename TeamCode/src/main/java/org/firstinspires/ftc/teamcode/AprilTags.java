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
    public void initAprilTag() {
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
    public List<Double> telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        //blue april tag
        double xPose20 = 0;
        double yPose20 = 0;
        double zPose20 = 0;
        double pitch20 = 0; //y-rotation
        double roll20 = 0; //x-rotation
        double yaw20 = 0; //z-rotation
        double range20 = 0; //distance
        double bearing20 = 0; //
        double elevation20 = 0; //
        //red april tag
        double xPose24 = 0;
        double yPose24 = 0;
        double zPose24 = 0;
        double pitch24 = 0;
        double roll24 = 0;
        double yaw24 = 0;
        double range24 = 0;
        double bearing24 = 0;
        double elevation24 = 0;

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == 20) {
                xPose20 = detection.ftcPose.x;
                yPose20 = detection.ftcPose.y;
                zPose20 = detection.ftcPose.z;
                pitch20 = detection.ftcPose.pitch;
                roll20 = detection.ftcPose.roll;
                yaw20 = detection.ftcPose.yaw;
                range20 = detection.ftcPose.range;
                bearing20 = detection.ftcPose.bearing;
                elevation20 = detection.ftcPose.elevation;
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", xPose20, yPose20, zPose20));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", pitch20, roll20, yaw20));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", range20, bearing20, elevation20));
            }else{
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
            if(detection.id == 24){
                xPose24 = detection.ftcPose.x;
                yPose24 = detection.ftcPose.y;
                zPose24 = detection.ftcPose.z;
                pitch24 = detection.ftcPose.pitch;
                roll24 = detection.ftcPose.roll;
                yaw24 = detection.ftcPose.yaw;
                range24 = detection.ftcPose.range;
                bearing24 = detection.ftcPose.bearing;
                elevation24 = detection.ftcPose.elevation;
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", xPose24, yPose24, zPose24));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", pitch24, roll24, yaw24));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", range24, bearing24, elevation24));
            }else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
        return List.of(xPose20, yPose20, zPose20, pitch20, roll20, yaw20, range20, bearing20, elevation20, xPose24, yPose24, zPose24, pitch24, roll24, yaw24, range24, bearing24, elevation24);
    }
}