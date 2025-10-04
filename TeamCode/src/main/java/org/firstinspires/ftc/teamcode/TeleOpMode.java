package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TeleOpMode extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    @Override
    public void init() {

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
        // TODO - April tag stuff (camera)
        // TODO - init color identification
        // TODO - init telemetry (display on the driver hub)
        // TODO - Init intake
        // TODO - init outtake
        // TODO - init distance sensors
        // TODO - init indicator light
    }

    @Override
    public void start() {
        super.start();

        // TODO - look for april tags
        // TODO - establish our coordinates / location from the april tag
    }

    @Override
    public void stop() {
        super.stop();

        // TODO - stop all motors
        // TODO - any final telemetry
    }

    @Override
    public void loop() {
        // Controller 1
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;
        double multiplier = 0.75;
        if (gamepad1.left_trigger < 0) {
            multiplier = 0.5;
        } else if (gamepad1.right_trigger < 0) {
            multiplier = 1;
        }
        frontLeftPower = (axial + lateral + yaw) * multiplier;
        frontRightPower = (axial - lateral - yaw) * multiplier;
        backLeftPower = (axial - lateral + yaw) * multiplier;
        backRightPower = (axial + lateral - yaw) * multiplier;


        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);


        // Controller 2
        // TODO - Left stick aiming for distance (motor speed)
        // TODO - right stick aiming for distance angle
        // TODO - X for auto aiming (overrides driver)
        // TODO - L/RT for shoot ball
        // TODO - B for intake on/off
        // TODO - A for outtake on/off

        // TODO - update telemetry
    }
}
