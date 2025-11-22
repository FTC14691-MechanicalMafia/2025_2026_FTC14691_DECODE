package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.firstinspires.ftc.teamcode.vision.AprilTagDriver;

import java.util.Arrays;
import java.util.List;
import java.util.*;

public abstract class MMDriveOpMode extends OpMode {
    /**
     * This is the list of april tag ids that we care about.
     */
    public static final List<Integer> APRIL_TAG_IDS = Arrays.asList(20, 24);
    protected DcMotor frontLeftDrive = null;
    protected DcMotor backLeftDrive  = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backRightDrive = null;
    protected DcMotor intakeDrive = null;
    protected DcMotor outtakeDrive = null;
    protected AprilTagDriver aprilTagDrive = null;
    protected Servo aimServo = null;
    protected Servo shootServo = null;

    @Override
    public void init() {
        // Init our drive motors (set 0 power behavior, direction)
        telemetry.addLine("Mecanum: Offline");

        //init drive system
        if (RobotConstants.DRIVE_ENABLED) {
            frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
            backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
            frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
            backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

            frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
            backRightDrive.setDirection(DcMotor.Direction.FORWARD);

            frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // TODO - Init our pinpoint driver / dead wheels
        telemetry.addLine("Pinpoint: Offline");

        // April tag stuff (camera)
        if (RobotConstants.CAMERA_ENABLED) {
            aprilTagDrive = new AprilTagDriver(telemetry, hardwareMap);
            aprilTagDrive.initAprilTag();
            telemetry.addLine("Camera: Online");
        }

        // TODO - init color identification
        telemetry.addLine("Color: Offline");

        // Init intake
        if (RobotConstants.INTAKE_ENABLED) {
            telemetry.addLine("Intake: Online");
            intakeDrive = hardwareMap.get(DcMotor.class, "intake_drive");
            intakeDrive.setDirection(DcMotor.Direction.FORWARD);
            intakeDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        //  init outtake
        if (RobotConstants.OUTTAKE_ENABLED) {
            telemetry.addLine("Outtake: Online");
            outtakeDrive = hardwareMap.get(DcMotor.class, "outtake_drive");
            outtakeDrive.setDirection(DcMotor.Direction.FORWARD);
            outtakeDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            aimServo = hardwareMap.get(Servo.class, "outtake_aim");
            aimServo.setPosition(RobotConstants.OUTTAKE_AIM_INIT_POS);
        }

        // TODO - init distance sensors
        telemetry.addLine("Distance: Offline");

        // TODO - init indicator light; Note - no telemetry needed since this is its own status

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

        // stop all motors
        if (RobotConstants.DRIVE_ENABLED) {
            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);
        }
        if (RobotConstants.INTAKE_ENABLED) {
            intakeDrive.setPower(0);
        }
        if (RobotConstants.OUTTAKE_ENABLED) {
            outtakeDrive.setPower(0);
        }
        // TODO - any final telemetry

    }

    /**
     * Returns a power to use for turning based on with april tag (blue or red) it currently sees.
     * This should orient the robot towards that april tag if used consistently in the main loop.
     * @return 0 if there is no tag, or already aimed within in the margin.  A value between -1 and 1 (not 0) that indicates the power to use.
     */
    public Integer autoAim(){
        //returns -1 for left turns, 0 for on target & 1 for right turns, null for no apriltag
        List<AprilTag> aprilTags = aprilTagDrive.detectAprilTags();
        Optional<AprilTag> optionalAprilTag = aprilTags.stream()
                .filter(aprilTag -> APRIL_TAG_IDS.contains(aprilTag.getId()))
                .findFirst();

        // set our default power
        int power = 0;

        // calculate the angles if we found a tag we care about
        if (optionalAprilTag.isPresent()) {
            AprilTag tag = optionalAprilTag.get();
            int angle = (int) Math.round(tag.getTargetting().getBearing());
            //TODO - make the margin configurable
            if (Math.abs(angle) > 2) {//2 is the margin of error on either side, can be changed with the circumstances
                power = angle / Math.abs(angle);
            }
        }

        //return whatever power we determined
        return power;
    }
}
