package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriver;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    // Drive System
    protected DcMotor frontLeftDrive = null;
    protected DcMotor backLeftDrive = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backRightDrive = null;
    protected String driveStatus = "Offline";

    // Positioning system
    protected GoBildaPinpointDriverRR odo; // Declare OpMode member for the Odometry Computer
    protected String odoStatus = "Offline";
    protected String odoPos = "None";
    protected String odoVel = "None";

    // Intake System
    protected DcMotor intakeDrive = null;
    protected String intakeStatus = "Offline";

    // Outtake System
    protected DcMotor outtakeDrive = null;
    protected Servo aimServo = null;
    protected Servo shootServo = null;
    protected String outtakeStatus = "Offline";
    protected AprilTagDriver aprilTagDrive = null;
    protected String aprilTagStatus = "Offline";

    public String getDriveStatus() {
        return driveStatus;
    }

    public String getOdoStatus() {
        return odoStatus;
    }

    public String getOdoPos() {
        return odoPos;
    }

    public String getOdoVel() {
        return odoVel;
    }

    public String getAprilTagStatus() {
        return aprilTagStatus;
    }

    public String getOuttakeStatus() {
        return outtakeStatus;
    }

    public String getIntakeStatus() {
        return intakeStatus;
    }

    @Override
    public void init() {
        // Init our drive motors (set 0 power behavior, direction)
        telemetry.addData("Mecanum: %s", this::getDriveStatus);
        if (RobotConstants.DRIVE_ENABLED) {
            frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
            backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
            frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
            backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

            frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
            backRightDrive.setDirection(DcMotor.Direction.REVERSE);

            frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            this.driveStatus = "Initialized";
        }

        // Init our pinpoint driver / dead wheels
        telemetry.addData("Pinpoint: %s", this::getOdoStatus);
        telemetry.addData("Position: %s", this::getOdoPos);
        telemetry.addData("Velocity: %s", this::getOdoVel);
        if (RobotConstants.ODO_ENABLED) {
            // Initialize the hardware variables. Note that the strings used here must correspond
            // to the names assigned during the robot configuration step on the DS or RC devices.
            odo = hardwareMap.get(GoBildaPinpointDriverRR.class, "pinpoint");
            // TODO - what are these values?
            odo.setOffsets(-84.0, -168.0); //these are tuned for 3110-0002-0001 Product Insight #1
            odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
            // Set the direction that each of the two odometry pods count. The X (forward) pod should
            // increase when you move the robot forward. And the Y (strafe) pod should increase when
            // you move the robot to the left.
            // TODO - what are these values?
            odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
            // Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
            odo.resetPosAndIMU();
            this.odoStatus = odo.getDeviceStatus().name();
        }

        // April tag stuff (camera)
        telemetry.addData("AprilTags: %s", this::getAprilTagStatus);
        if (RobotConstants.CAMERA_ENABLED) {
            aprilTagDrive = new AprilTagDriver(telemetry, hardwareMap);
            aprilTagDrive.initAprilTag();

            this.aprilTagStatus = "Initialized";
        }

        // TODO - init color identification
        telemetry.addData("Color: %s", "Offline");

        // Init intake
        telemetry.addData("Intake: %s", this::getIntakeStatus);
        if (RobotConstants.INTAKE_ENABLED) {
            intakeDrive = hardwareMap.get(DcMotor.class, "intake_drive");
            intakeDrive.setDirection(DcMotor.Direction.FORWARD);
            intakeDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            this.intakeStatus = "Initialized";
        }

        //  init outtake
        telemetry.addData("Outtake: %s", this::getOuttakeStatus);
        if (RobotConstants.OUTTAKE_ENABLED) {
            outtakeDrive = hardwareMap.get(DcMotor.class, "outtake_drive");
            outtakeDrive.setDirection(DcMotor.Direction.FORWARD);
            outtakeDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            aimServo = hardwareMap.get(Servo.class, "outtake_aim");
            aimServo.setPosition(RobotConstants.OUTTAKE_AIM_INIT_POS);

            this.outtakeStatus = "Initialized";
        }

        // TODO - init distance sensors
        telemetry.addData("Distance: %s", "Offline");

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
            this.driveStatus = "Stopped";
        }
        if (RobotConstants.INTAKE_ENABLED) {
            intakeDrive.setPower(0);
            this.intakeStatus = "Stopped";
        }
        if (RobotConstants.OUTTAKE_ENABLED) {
            outtakeDrive.setPower(0);
            this.outtakeStatus = "Stopped";
        }
        // TODO - any final telemetry
    }

    /**
     * Returns a power to use for turning based on with april tag (blue or red) it currently sees.
     * This should orient the robot towards that april tag if used consistently in the main loop.
     *
     * @return 0 if there is no tag, or already aimed within in the margin.  A value between -1 and 1 (not 0) that indicates the power to use.
     */
    public Double autoAim(final List<AprilTag> aprilTags) {
        //returns -1 for left turns, 0 for on target & 1 for right turns, null for no apriltag
        Optional<AprilTag> optionalAprilTag = aprilTags.stream()
                .filter(aprilTag -> APRIL_TAG_IDS.contains(aprilTag.getId()))
                .findFirst();

        // set our default power
        double power = 0;

        // calculate the angles if we found a tag we care about
        if (optionalAprilTag.isPresent()) {
            AprilTag tag = optionalAprilTag.get();
            int angle = (int) Math.round(tag.getTargetting().getBearing());
            //TODO - make the margin configurable
            if (Math.abs(angle) > 1) {//2 is the margin of error on either side, can be changed with the circumstances
                power = angle * Math.abs(angle) * 0.1 / -Math.abs(angle);
            }
        }

        //return whatever power we determined
        return power;
    }
}
