package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;

public class ShooterActions {
    DcMotorEx outtakeDrive;
    Servo kicker;

    public ShooterActions(DcMotorEx outtakeDrive, Servo kicker) {
        this.outtakeDrive = outtakeDrive;
        this.kicker = kicker;
    }


    public Action setShooterPower(double power) {
        return new Action() {
            @Override
            public boolean run(TelemetryPacket telemetryPacket) {
                outtakeDrive.setPower(power);
                return false;
            }
        };
    }
    public Action kick(double Pos) {
        return new Action() {
            @Override
            public boolean run(TelemetryPacket telemetryPacket) {
                kicker.setPosition(Pos);
                new SleepAction(1).run(telemetryPacket);
                kicker.setPosition(RobotConstants.OUTTAKE_SHOOT_REST_POS);
                return true;
            }
        };
    }

}
