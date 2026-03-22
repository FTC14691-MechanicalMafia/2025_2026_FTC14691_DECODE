package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Velocity Test", group = "Competition")

public class SetVelocityTest extends OpMode {
    boolean shooting = false;
    double velocity = 100; // should be modified through testing
    DcMotorEx outtakeDrive1;
    DcMotorEx outtakeDrive2;
    DcMotor intakeDrive;
    Servo aimServ;
    Servo turnServ;
    double outVel1;
    double outVel2;
    double inPow;
    double timeStor = 0.0; // in seconds
    @Override
    public void init(){
        outtakeDrive1 = hardwareMap.get(DcMotorEx.class, "outtake1");
        outtakeDrive2 = hardwareMap.get(DcMotorEx.class, "outtake2");
        intakeDrive = hardwareMap.get(DcMotor.class, "intake");
        aimServ = hardwareMap.get(Servo.class, "aimer");
        turnServ = hardwareMap.get(Servo.class, "turner");
        telemetry.addLine("velocity: ");
    }

    @Override
    public void loop() {
        outtakeDrive1.setDirection(DcMotorEx.Direction.REVERSE);
        outtakeDrive2.setDirection(DcMotorEx.Direction.FORWARD);
        if(gamepad2.dpad_up){
            aimServ.setPosition(aimServ.getPosition() + 0.005);
        } else if (gamepad2.dpad_down){
            aimServ.setPosition(aimServ.getPosition() - 0.01);
        }

        if(gamepad2.left_trigger > 0 && getRuntime() > timeStor + 0.5){
            if(shooting){
                outVel1 = 0;
                outVel2 = 0;
                shooting = false;
            } else {
                outVel1 = velocity; //
                outVel2 = velocity; //
                shooting = true;
            }
            timeStor = getRuntime();
        }

        if(gamepad2.right_trigger > 0){
            if(shooting){
                outVel1 += 2;
                outVel2 += 2;
            }
        } else if (gamepad2.right_bumper){
            if(shooting){
                outVel1--;
                outVel2--;
            }
        }
        if(outVel1 < 0 || outVel2 < 0){
            outVel1 = 0;
            outVel2 = 0;
            shooting = false;
        }

        for(int i = 0; i < gamepad2.left_stick_x; i++){
            turnServ.setPosition(turnServ.getPosition() + 1);
        }
        for(int i = 0; i > gamepad2.left_stick_x; i--){
            turnServ.setPosition(turnServ.getPosition() - 1);
        }

        outtakeDrive1.setVelocity(outVel1, AngleUnit.RADIANS);
        outtakeDrive2.setVelocity(outVel2, AngleUnit.RADIANS);
        telemetry.addData("velocity: ", outVel1);
    }
}
