package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")
public class PickUp extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftIntakeDrive = null;
    private DcMotor rightIntakeDrive = null;


    public void runOpMode(){
        leftIntakeDrive = hardwareMap.get(DcMotor.class, "left_intake_drive");
        rightIntakeDrive = hardwareMap.get(DcMotor.class, "right_intake_drive");
        //one drive must be reversed but this orientation may be subject to change
        leftIntakeDrive.setDirection(DcMotor.Direction.REVERSE);
        rightIntakeDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()){
            double intakePower = gamepad2.x ? 1.0 : 0.0;

            leftIntakeDrive.setPower(intakePower);
            rightIntakeDrive.setPower(intakePower);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Intake", "%4.2f", intakePower);
            telemetry.update();
        }
    }
}
