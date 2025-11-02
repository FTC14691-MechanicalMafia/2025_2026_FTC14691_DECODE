package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.List;

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
    protected AprilTagDriver aprilTagDriver = null;
    private Servo aimServo = null;

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

            frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
            backRightDrive.setDirection(DcMotor.Direction.REVERSE);

            frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // TODO - Init our pinpoint driver / dead wheels
        telemetry.addLine("Pinpoint: Offline");
        // TODO - April tag stuff (camera)
        if (RobotConstants.CAMERA_ENABLED) {
            aprilTagDriver = new AprilTagDriver(telemetry, hardwareMap);
            aprilTagDriver.initAprilTag();
        }

        telemetry.addLine("Camera: Offline");
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
        }

        // TODO - init telemetry (display on the driver hub)

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
}
