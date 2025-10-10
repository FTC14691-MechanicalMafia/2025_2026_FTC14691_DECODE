package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
@TeleOp
public class TeleOpMode extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive  = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor intake = null;
    private DcMotor outtake = null;

    AprilTags april; //creates object for AprilTags

    @Override
    public void init() {
        // Init our drive motors (set 0 power behavior, direction)
        telemetry.addLine("Mecanum: Offline");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // TODO - Init our pinpoint driver / dead wheels
        telemetry.addLine("Pinpoint: Offline");
        // TODO - April tag stuff (camera)
        telemetry.addLine("Camera: Offline");
        // TODO - init color identification
        telemetry.addLine("Color: Offline");
        // Init intake
        telemetry.addLine("Intake: Offline");
        intake = hardwareMap.get(DcMotor.class, "intake"); //CHANGE PLZ!!
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //  init outtake
        telemetry.addLine("Outtake: Offline");
        outtake = hardwareMap.get(DcMotor.class, "outtake"); //CHANGE PLZ!!
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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

        List<Double> info = april.telemetryAprilTag();
        //list data is (xPose, yPose, zPose, pitch, roll, yaw, range, bearing, elevation)
        telemetry.addData("x pose is " + info.get(0), "inches");
        telemetry.addData("Y pose is " + info.get(1), "inches");
        telemetry.addData("Z pose is " + info.get(2), "inches");
        telemetry.addData("Pitch is " + info.get(3), "degrees");
        telemetry.addData("Roll is " + info.get(4), "degrees");
        telemetry.addData("Yaw is " + info.get(5), "degrees");
        telemetry.addData("Range is " + info.get(6), "inches");
        telemetry.addData("Bearing is " + info.get(7), "degrees");
        telemetry.addData("Elevation is " + info.get(8), "inches");
        // TODO - establish our coordinates / location from the april tag
    }

    @Override
    public void stop() {
        super.stop();

        // stop all motors
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        intake.setPower(0);
        outtake.setPower(0);
        // TODO - any final telemetry
    }

    @Override
    public void loop() {
        april.runOpMode();
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
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);


        // Controller 2
        // TODO - Left stick aiming for distance (motor speed)
        // TODO - right stick aiming for distance angle
        // TODO - X for auto aiming (overrides driver)
        // TODO - L/RT for shoot ball
        // B for intake on/off
        intake.setPower(gamepad2.b ? 1.0 : 0.0);
        // A for outtake on/off
        outtake.setPower(gamepad2.a ? 1.0 : 0.0);

        //update the coordinates
        telemetry.addLine("Mecanum: Offline");
        telemetry.addLine("Pinpoint: Offline");
        telemetry.addLine("Camera: Offline");
        telemetry.addLine("Color: Offline");
        telemetry.addLine("Intake: Offline");
        telemetry.addLine("Outtake: Offline");
        telemetry.addLine("Distance: Offline");

        // update telemetry
        telemetry.update();
    }
}
