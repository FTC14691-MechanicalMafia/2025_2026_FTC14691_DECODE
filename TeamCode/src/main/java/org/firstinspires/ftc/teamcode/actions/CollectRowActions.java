package org.firstinspires.ftc.teamcode.actions;

import org.firstinspires.ftc.teamcode.opmode.RRAutoOpMode;
import org.firstinspires.ftc.teamcode.RobotConstants;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmode.RRAutoOpMode;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.firstinspires.ftc.teamcode.vision.AprilTagDriver;

import java.util.*;

public class CollectRowActions{
    RobotConstants constants = new RobotConstants();
    public enum Rows {
        close, middle, far //from goals

    }
    private TrajectoryActionBuilder builder;
    double[] placesCoords = new double[9];
    DcMotor intakeDrive;
    public CollectRowActions(TrajectoryActionBuilder builder, DcMotor intakeDrive){
        this.builder = builder;
        this.intakeDrive = intakeDrive;
    }
    public class ToRowAction implements Action {
        private int row;//later figure out how to use Rows option bc Rows was causing issue with location call

        public ToRowAction(int row) {
            this.row = row;
        }
        public Action setIntakePower(double power) {
            Action action = new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    intakeDrive.setPower(power);
                    //setVelocity();
                    return true;
                }
            };
            return action;
        }




        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (constants.team.equalsIgnoreCase("blue")) {
                for(int i = 0; i < constants.Blue_ballPlaces.length; i++){
                    placesCoords[i] = constants.Red_ballPlaces[i];
                }
                placesCoords[0] += 1.25;
                placesCoords[2] += 1.25;
                placesCoords[4] += 1.25;
                placesCoords[6] = placesCoords[0] - 2;
                placesCoords[7] = placesCoords[2] - 2;
                placesCoords[8] = placesCoords[4] - 2;
            } else if (constants.team.equalsIgnoreCase("red")) {
                for(int i = 0; i < constants.Red_ballPlaces.length; i++) {
                    placesCoords[i] = constants.Red_ballPlaces[i];
                }
                placesCoords[0] -= 1.25;
                placesCoords[2] -= 1.25;
                placesCoords[4] -= 1.25;
                placesCoords[6] = placesCoords[0] + 2;
                placesCoords[7] = placesCoords[2] + 2;
                placesCoords[8] = placesCoords[4] + 2;
            }
            new SequentialAction(
                    builder.splineTo(new Vector2d(placesCoords[(row*2)], placesCoords[(row*2) + 1]), 90).build(),
                    setIntakePower(0.8),
                    builder.lineToX((placesCoords[row*2] / (Math.abs(placesCoords[row*2]))) * (Math.abs(placesCoords[(row*2)]) + constants.rowLength)).build(),
                    setIntakePower(0.0)
            );
            return true;
        }
    }
    public Action toRowAction(int row){
        return new ToRowAction(row);
    }
}
