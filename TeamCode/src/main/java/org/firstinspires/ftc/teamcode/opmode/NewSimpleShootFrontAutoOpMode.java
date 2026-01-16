package org.firstinspires.ftc.teamcode.opmode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.actions.CollectRowActions;
import org.firstinspires.ftc.teamcode.actions.ShooterActions;

@Autonomous(name="New Simple Shoot Front")
public class NewSimpleShootFrontAutoOpMode extends RRAutoOpMode {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    final RobotConstants constants = new RobotConstants();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    public void start() {
        super.start();

        // Get our trajectory builder to add actions to
        TrajectoryActionBuilder builder = mecanumDrive.actionBuilder(getInitialPose());

        ShooterActions shooterActions = new ShooterActions(outtakeDrive, kicker);
        CollectRowActions collectRowActions = new CollectRowActions(builder, intakeDrive);
        SleepAction sleepAction = new SleepAction(0.75);

        Action kickAction = new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                kicker.setPosition(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS);
                new SleepAction(10).run(telemetryPacket);
                //kicker.setPosition(RobotConstants.OUTTAKE_SHOOT_REST_POS),
                return true;
            }
        };

        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        //move back 4 ft, spin up motor
//                        new ParallelAction(
//                                builder.lineToX(10).build(),
//                                shooterActions.setShooterPower(0.8)
//                        ),

                        //intake on
                        collectRowActions.setIntakePower(1),

                        //kick
                        sleepAction,

                        //wait
                        sleepAction,

                        //kick
                        sleepAction,

                        //wait
                        sleepAction,

                        //kick
                        sleepAction,

                        //stop motor, intake off
                        new ParallelAction(
                                shooterActions.setShooterPower(0),
                                collectRowActions.setIntakePower(0)
                        )

                        )

        );
    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 0;
        public int positionY = 0;
        public int heading = 0;
    }

}
