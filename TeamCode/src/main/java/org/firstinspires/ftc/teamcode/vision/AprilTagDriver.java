package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.ftccommon.internal.manualcontrol.ManualControlOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AprilTagDriver {
    private AprilTagProcessor processor;
    private VisionPortal visionPortal;
    private ManualControlOpMode manualControl;

    public final Telemetry telemetry;

    public final HardwareMap hardwareMap;

    public AprilTagDriver(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }

    public void initAprilTag() {
        //suggest fx = 482.757, fy = 482.757, cx = 270.511, cy = 196.022
        double fx = 2000;//x-direction focal length
        double fy = 600;//y-direction focal length
        double cx = 300;//principal point x coord
        double cy = 300;//principal point y coord
        manualControl = new ManualControlOpMode();
        processor = new AprilTagProcessor.Builder()
                .setLensIntrinsics(fx, fy, cx, cy)
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCameraResolution(new Size(640, 480));
        if (RobotConstants.CAMERA_ENABLED) {
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
    public List<AprilTag> detectAprilTags() {
        if (processor == null) {
            // added during competition to make auto op modes work again
            return Collections.EMPTY_LIST;
        }

        List<AprilTagDetection> currentDetections = processor.getDetections();
        telemetry.addData("Number AprilTags Detected", currentDetections.size());

        // handle all of the detections
        double trunNum = 0.01;
        final List<AprilTag> aprilTags = currentDetections.stream()
                .filter(Objects::nonNull)
                .filter(detection -> detection.ftcPose != null)
                .map(detection -> {
                    final Pose pose = new Pose(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z);
                    final Rotation rotation = new Rotation(detection.ftcPose.roll, detection.ftcPose.pitch, detection.ftcPose.yaw);
                    final Targeting targeting = new Targeting(detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation);
                    return new AprilTag(detection.id, pose, rotation, targeting);
                })
                .collect(Collectors.toList());

        return aprilTags;
    }
}