package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp
public class TeleOpMode extends OpMode {

    /**
     * This is the list of april tag ids that we care about.
     */
    public static final List<Integer> APRIL_TAG_IDS = Arrays.asList(20, 24);

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive  = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor intakeDrive = null;
    private DcMotor outtakeDrive = null;
    private Servo aimServo = null;
    // private DcMotor intake = null;
    //private DcMotor outtake = null;

    private AprilTagDriver aprilTagDriver = null;

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
        intakeDrive = hardwareMap.get(DcMotor.class, "intake_drive");
        outtakeDrive = hardwareMap.get(DcMotor.class, "outtake_drive");
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
        telemetry.addLine("Intake: Offline");
        //intake = hardwareMap.get(DcMotor.class, "intake"); //CHANGE PLZ!!
        //intake.setDirection(DcMotorSimple.Direction.FORWARD);
        //intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //  init outtake
        telemetry.addLine("Outtake: Offline");
        //outtake = hardwareMap.get(DcMotor.class, "outtake"); //CHANGE PLZ!!
        //outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        //outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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
        intakeDrive.setPower(0);
        outtakeDrive.setPower(0);
        // TODO - any final telemetry

    }

    @Override
    public void loop() {
        // Controller 1

        // implement left stick for mechanic forward/strafe
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral = gamepad1.left_stick_x;
        // implement right stick for rotation
        double yaw = gamepad1.right_stick_x;

        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;
        double multiplier = 0.75;
        if (gamepad1.left_trigger < 0) {
            //  LT for slow
            multiplier = 0.5;
        } else if (gamepad1.right_trigger < 0) {
            //  RT for boost
            multiplier = 1;
        }
        frontLeftPower = (axial + lateral + yaw) * multiplier;
        frontRightPower = (axial - lateral - yaw) * multiplier;
        backLeftPower = (axial - lateral + yaw) * multiplier;
        backRightPower = (axial + lateral - yaw) * multiplier;

        // set the drive power
        if (RobotConstants.DRIVE_ENABLED) {
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);
        }


        // Controller 2

        // TODO - Left stick aiming for distance (motor speed)
        // TODO - right stick aiming for distance angle
        // TODO - X for auto aiming (overrides driver)
        // TODO - L/RT for shoot ball
        // B for intake on/off
        intakeDrive.setPower(gamepad2.b ? 1.0 : 0.0);
        // A for outtake on/off
        outtakeDrive.setPower(gamepad2.a ? 1.0 : 0.0);

        //update the coordinates
        telemetry.addLine("Mecanum: Offline");
        telemetry.addLine("Pinpoint: Offline");
        telemetry.addLine("Camera: Offline");
        telemetry.addLine("Color: Offline");
        telemetry.addLine("Intake: Offline");
        telemetry.addLine("Outtake: Offline");
        telemetry.addLine("Distance: Offline");

        if (RobotConstants.CAMERA_ENABLED) {
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

        }
        // update telemetry
        telemetry.update();
    }
}
