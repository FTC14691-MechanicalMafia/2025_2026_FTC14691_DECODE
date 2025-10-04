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
    private DcMotor intake = null;
    private DcMotor outtake = null;



    @Override
    public void init() {
        // TODO - Init our drive motors (set 0 power behavior, direction)
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
        // TODO - Init intake
        telemetry.addLine("Intake: Offline");
        // TODO - init outtake
        telemetry.addLine("Outtake: Offline");
        // TODO - init telemetry (display on the driver hub)
        intake = hardwareMap.get(DcMotor.class, "intake"); //CHANGE PLZ!!
        outtake = hardwareMap.get(DcMotor.class, "outtake"); //CHANGE PLZ!!

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // TODO - init distance sensors
        telemetry.addLine("Distance: Offline");
        // TODO - init indicator light; Note - no telemetry needed since this is its own status

        // init telemetry (display on the driver hub)
        telemetry.update();
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
        // TODO - RT for boost
        // TODO - LT for slow
        // TODO - implement left stick for mechanic forward/strafe
        // TODO - implement right stick for rotation
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
        intake.setPower(gamepad2.b ? 1.0 : 0.0);
        outtake.setPower(gamepad2.a ? 1.0 : 0.0);
        // TODO - A for outtake on/off

        // TODO - update the coordinates
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
