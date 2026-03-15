package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.actions.CollectRowActions;
import org.firstinspires.ftc.teamcode.actions.ShooterActions;

import java.util.Arrays;

@Autonomous(name="New Simple Shoot Front")
public class NewSimpleShootFrontAutoOpMode extends RRAutoOpMode {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    final RobotConstants constants = new RobotConstants();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    public Action kickUpAction() {
        return new Action() {
            @Override
            public boolean run(TelemetryPacket telemetryPacket) {
                kicker.setPosition(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS);
                return false; // finishes immediately
            }
        };
    }

    public Action kickDownAction() {
        return new Action() {
            @Override
            public boolean run(TelemetryPacket telemetryPacket) {
                kicker.setPosition(RobotConstants.OUTTAKE_SHOOT_REST_POS);
                return false;
            }
        };
    }


    @Override
    public void start() {
        super.start();

        // Get our trajectory builder to add actions to
        TrajectoryActionBuilder builder = mecanumDrive.actionBuilder(getInitialPose());

        ShooterActions shooterActions = new ShooterActions(outtakeDrive, kicker);
        CollectRowActions collectRowActions = new CollectRowActions(builder, intakeDrive);


        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        //move back 4 ft, spin up motor
                        new ParallelAction(
                                builder.lineToX(RobotConstants.AUTO_GOBACK_DIST).build(),
//                                builder.lineToX(
//                                        RobotConstants.AUTO_GOBACK_DIST,
//                                        new MinVelConstraint(Arrays.asList(
//                                                new TranslationalVelConstraint(RobotConstants.AUTO_ACCEL_CONSTRAINT), // Max Speed in inches/sec
//                                                new AngularVelConstraint(Math.PI / 2) // Max Angular Speed
//                                        )),
//                                        new ProfileAccelConstraint(-20.0, 25.0) // Min and Max Acceleration
//                                ).build(),
                                shooterActions.setShooterPower(RobotConstants.AUTO_OUTTAKE_POWER_1)
                        ),


                        //intake on
                        collectRowActions.setIntakePower(RobotConstants.AUTO_INTAKE_POWER),

                        //wait
                        new SleepAction(RobotConstants.AUTO_INITIAL_KICK_DELAY),

                        //kick
                        autoActionName("shoot #1"),
                        kickUpAction(),
                        new SleepAction(RobotConstants.AUTO_KICK_UPDOWN_DELAY),
                        kickDownAction(),

                        //wait
                        autoActionName("wait #1"),
                        new ParallelAction(
                            new SleepAction(RobotConstants.AUTO_KICK_WAIT_DELAY),
                            shooterActions.setShooterPower(RobotConstants.AUTO_OUTTAKE_POWER_2)
                        ),
                        

                        //kick
                        autoActionName("shoot #2"),
                        kickUpAction(),
                        new SleepAction(RobotConstants.AUTO_KICK_UPDOWN_DELAY),
                        kickDownAction(),

                        //wait
                        autoActionName("wait #2"),
                        new ParallelAction(
                                new SleepAction(RobotConstants.AUTO_KICK_WAIT_DELAY),
                                shooterActions.setShooterPower(RobotConstants.AUTO_OUTTAKE_POWER_3)
                        ),

                        //kick
                        autoActionName("shoot #3"),
                        kickUpAction(),
                        new SleepAction(RobotConstants.AUTO_KICK_UPDOWN_DELAY),
                        kickDownAction(),

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
