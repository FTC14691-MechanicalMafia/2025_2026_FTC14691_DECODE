package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class OuttakeTestOpMode extends OpMode {
    private DcMotorEx Motor1 = null;
    private DcMotorEx Motor2 = null;
    private Servo servo = null;
    private boolean motorOn = false;
    private int ticksPerRevolution = 28;

    @Override
    public void init(){
        Motor1 = hardwareMap.get(DcMotorEx.class, "motor_1");
        Motor1.setDirection(DcMotor.Direction.FORWARD);
        Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor2 = hardwareMap.get(DcMotorEx.class, "motor_2");
        Motor2.setDirection(DcMotor.Direction.REVERSE);
        Motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        servo = hardwareMap.get(Servo.class, "servo");
    }
    public void loop(){
        double outtakePower = 0.5;
        if (gamepad1.a){
            motorOn = true;
        }
        if (gamepad1.b){
            motorOn = false;
        }
        if (motorOn) {
//            Motor1.setVelocity(ticksPerRevolution*60);
//            Motor2.setVelocity(ticksPerRevolution*60);
            Motor1.setPower(outtakePower);
            Motor2.setPower(outtakePower);
        }

        double servoPos = servo.getPosition();
        double servoIncrement = 0.001;
        if (gamepad1.y) {
            servo.setPosition(servoPos+servoIncrement);
        }
        else if (gamepad1.x) {
            servo.setPosition(servoPos-servoIncrement);
        }

        telemetry.addData("Servo orig:", servoPos);
        telemetry.addData("Servo pos:", servo.getPosition());
        telemetry.addData("Motor 1 spd:", Motor1.getVelocity());
        telemetry.addData("Motor 2 spd:", Motor2.getVelocity());
        telemetry.addData("Motor 1 pwr:", Motor1.getPower());
        telemetry.addData("Motor 2 spd:", Motor2.getPower());


        telemetry.update();
    }

    public void stop(){
        Motor1.setPower(0);
        Motor2.setPower(0);
    }
}
