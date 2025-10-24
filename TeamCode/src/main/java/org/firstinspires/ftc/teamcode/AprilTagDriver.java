package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.hardware.HardwareManualControlOpMode;
import java.util.*;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.AprilDetections;
import org.firstinspires.ftc.teamcode.Targetting;
import org.firstinspires.ftc.teamcode.Rotation;
import org.firstinspires.ftc.teamcode.Pose;


import org.firstinspires.ftc.ftccommon.internal.manualcontrol.ManualControlOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;


import java.util.List;

public class AprilTagDriver {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private AprilTagProcessor processor;
    private VisionPortal visionPortal;
    private ManualControlOpMode manualControl;

    public final Telemetry telemetry;

    public final HardwareMap hardwareMap;

    public AprilTagDriver(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }
    public int patternDetect(){
        List<AprilTagDetection> currentDetections = processor.getDetections();
        int aprilDetect = 0;
        for (AprilTagDetection detection : currentDetections) {
            if(detection.id > 20 && detection.id < 24){
                aprilDetect = detection.id;
            }
        }
        return aprilDetect;
    }
    public void initAprilTag() {
        double fx = 10;//x-direction focal length
        double fy = 10;//y-direction focal length
        double cx = 5;//principal point x coord
        double cy = 5;//principal point y coord
        manualControl = new ManualControlOpMode();
        processor = new AprilTagProcessor.Builder()
                .setLensIntrinsics(fx, fy, cx, cy)
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCameraResolution(new Size(640, 480));
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builder.addProcessor(processor);
        /*visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam"), processor);//allows for camera settings to be adjusted
        visionPortal.getCameraControl(manualControl).setExposureControl(ExposureControl.Mode.Manual);//sets the control for camera settings to MANUAL
        GainControl gainControl = visionPortal.getCameraControl(GainControl.Mode.MANUAL()).getGainControl();
        gainControl.setGain(100); // Set gain to 100 (example value)*/
        visionPortal = builder.build();
    }
    public List<Double> telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = processor.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        //blue april tag
        double xPose20 = 0;
        double yPose20 = 0;
        double zPose20 = 0;
        List<Double> pose20 = Arrays.asList(xPose20,yPose20,zPose20);
        double pitch20 = 0; //y-rotation
        double roll20 = 0; //x-rotation
        double yaw20 = 0; //z-rotation
        List<Double> rotation20 = Arrays.asList(pitch20,roll20,yaw20);
        double range20 = 0; //distance
        double bearing20 = 0; //
        double elevation20 = 0; //
        List<Double> targeting20 = Arrays.asList(range20,bearing20,elevation20);

        //red april tag
        double xPose24 = 0;
        double yPose24 = 0;
        double zPose24 = 0;
        List<Double> pose24 = Arrays.asList(xPose24,yPose24,zPose24);
        double pitch24 = 0;
        double roll24 = 0;
        double yaw24 = 0;
        List<Double> rotation24 = Arrays.asList(pitch24,roll24,yaw24);
        double range24 = 0;
        double bearing24 = 0;
        double elevation24 = 0;
        List<Double> targeting24 = Arrays.asList(range24,bearing24,elevation24);


        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == 20) {
                xPose20 = detection.ftcPose.x;
                yPose20 = detection.ftcPose.y;
                zPose20 = detection.ftcPose.z;
                pose20 = Arrays.asList(xPose20,yPose20,zPose20);

                pitch20 = detection.ftcPose.pitch;
                roll20 = detection.ftcPose.roll;
                yaw20 = detection.ftcPose.yaw;
                rotation20 = Arrays.asList(pitch20,roll20,yaw20);

                range20 = detection.ftcPose.range;
                bearing20 = detection.ftcPose.bearing;
                elevation20 = detection.ftcPose.elevation;
                targeting20 = Arrays.asList(range20,bearing20,elevation20);

                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", xPose20, yPose20, zPose20));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", pitch20, roll20, yaw20));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", range20, bearing20, elevation20));
            }
            if(detection.id == 24){
                xPose24 = detection.ftcPose.x;
                yPose24 = detection.ftcPose.y;
                zPose24 = detection.ftcPose.z;
                pose24 = Arrays.asList(xPose24,yPose24,zPose24);

                pitch24 = detection.ftcPose.pitch;
                roll24 = detection.ftcPose.roll;
                yaw24 = detection.ftcPose.yaw;
                rotation24 = Arrays.asList(pitch24,roll24,yaw24);

                range24 = detection.ftcPose.range;
                bearing24 = detection.ftcPose.bearing;
                elevation24 = detection.ftcPose.elevation;
                targeting24 = Arrays.asList(range24,bearing24,elevation24);

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