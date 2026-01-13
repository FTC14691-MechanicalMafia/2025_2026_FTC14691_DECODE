package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.actions.ShooterActions;

@Autonomous(name="Simple Shoot Front")
public class SimpleShootFrontAutoOpMode extends RRAutoOpMode {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

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
        SleepAction sleepAction = new SleepAction(0.75);

        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        autoActionName("Turnon"),
                        shooterActions.setShooterPower(0.8),

                        autoActionName("Kick"),
                        shooterActions.kick(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS),

                        autoActionName("Wait"),
                        sleepAction,

                        autoActionName("Kick"),
                        shooterActions.kick(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS),

                        autoActionName("Wait"),
                        sleepAction,

                        autoActionName("Kick"),
                        shooterActions.kick(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS),

                        new ParallelAction(
                            autoActionName("Turnoff"),
                            shooterActions.setShooterPower(0),

                            autoActionName("Move"),
                            builder.lineToX(-10).build() //CHANGE
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
