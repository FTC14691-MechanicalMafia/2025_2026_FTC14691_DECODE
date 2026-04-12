package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotConstants;

import java.util.Locale;

@TeleOp(name = "Demo Mode", group = "Competition")
public class DemoOpMode extends OpMode {
    boolean shooting = false;
    double velocity = 100; // should be modified through testing
    DcMotor FLDrive;
    DcMotor FRDrive;
    DcMotor BLDrive;
    DcMotor BRDrive;
    DcMotorEx outtakeDrive1;
    DcMotorEx outtakeDrive2;
    DcMotor intakeDrive;
    DcMotor kickDrive;
    Servo aimServ;
    Servo turnServ;
    double outVel1;
    double outVel2;
    double inPow;
    double timeStor = 0.0; // in seconds
    String driveStatus = "";

    @Override
    public void init() {
        FLDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        FRDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        BLDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        BRDrive = hardwareMap.get(DcMotor.class, "back_right_drive");
        outtakeDrive1 = hardwareMap.get(DcMotorEx.class, "outtake1");
        outtakeDrive2 = hardwareMap.get(DcMotorEx.class, "outtake2");
        //intakeDrive = hardwareMap.get(DcMotor.class, "intake");
        //kickDrive = hardwareMap.get(DcMotor.class, "kicker");
        aimServ = hardwareMap.get(Servo.class, "aimer");
        turnServ = hardwareMap.get(Servo.class, "turner");
        telemetry.addLine("shoot velocity: ");
        telemetry.addLine("speedRate: ");
        telemetry.addLine("turret angle: ");
    }

    @Override
    public void loop(){

        FLDrive.setDirection(DcMotor.Direction.REVERSE);
        FRDrive.setDirection(DcMotor.Direction.FORWARD);
        BLDrive.setDirection(DcMotor.Direction.REVERSE);
        BRDrive.setDirection(DcMotor.Direction.FORWARD);
        outtakeDrive1.setDirection(DcMotorEx.Direction.REVERSE);
        outtakeDrive2.setDirection(DcMotorEx.Direction.FORWARD);

        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        double driveMultiplier = 0.75;
        String speed = "normal";

        if(gamepad1.left_bumper){
            driveMultiplier = 0.5;
            speed = "slow";
        }
        if(gamepad1.right_bumper){
            driveMultiplier = 1;
            speed = "fast";
        }
        telemetry.addData("speedRate: ", speed);

        double max;

        double FLPow  = driveMultiplier*(axial + lateral + yaw);
        double FRPow = driveMultiplier*(axial - lateral - yaw);
        double BLPow  = driveMultiplier*(axial - lateral + yaw);
        double BRPow  = driveMultiplier*(axial + lateral - yaw);

        max = Math.max(Math.abs(FLPow), Math.abs(FRPow));
        max = Math.max(max, Math.abs(BLPow));
        max = Math.max(max, Math.abs(BRPow));

        if (max > 1.0) {
            FLPow  /= max;
            FRPow /= max;
            BLPow   /= max;
            BRPow  /= max;
        }

        FLDrive.setPower(FLPow);
        FRDrive.setPower(FRPow);
        BLDrive.setPower(BLPow);
        BRDrive.setPower(BRPow);

        // turret code
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
            turnServ.setPosition(turnServ.getPosition() + 0.01);
        }
        for(int i = 0; i > gamepad2.left_stick_x; i--){
            turnServ.setPosition(turnServ.getPosition() - 0.01);
        }
        if(turnServ.getPosition() < 0){
            turnServ.setPosition(0);
        }
        else if(turnServ.getPosition() > 1){
            turnServ.setPosition(1);
        }
        outtakeDrive1.setVelocity(outVel1, AngleUnit.RADIANS);
        outtakeDrive2.setVelocity(outVel2, AngleUnit.RADIANS);
        telemetry.addData("velocity: ", outVel1);
        telemetry.addData("turret angle: ", turnServ.getPosition());

        //intake & kick code
        /*double inPow = gamepad1.left_trigger;
        inPow = Math.min(inPow, 1);
        intakeDrive.setPower(inPow);

        double kickPow = gamepad1.right_trigger;
        kickPow = Math.min(kickPow, 1);
        kickDrive.setPower(kickPow);

        if(kickPow > 0){
            telemetry.addLine("kicker on");
        }
        if(inPow > 0){
            telemetry.addLine("intake on");
        }*/
    }
}
