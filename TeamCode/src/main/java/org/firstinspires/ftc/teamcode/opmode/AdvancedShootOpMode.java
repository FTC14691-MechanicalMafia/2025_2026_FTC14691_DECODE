package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.actions.CollectRowActions;
import org.firstinspires.ftc.teamcode.actions.ShooterActions;

public class AdvancedShootOpMode extends RRAutoOpMode{
    double shootX = 0;
    double shootY = 0;
    double angle = 0;
    public static SimpleMoveAutoOpMode.Params PARAMS = new SimpleMoveAutoOpMode.Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    public void start() {
        if (constants.team.equalsIgnoreCase("blue")) {
            shootX = constants.blueShoot[0];
            shootY = constants.blueShoot[1];
            angle = constants.blueShoot[2];
        } else if (constants.team.equalsIgnoreCase("red")) {
            shootX = constants.redShoot[0];
            shootY = constants.redShoot[1];
            angle = constants.redShoot[2];
        }
        super.start();

        // Get our trajectory builder to add actions to
        TrajectoryActionBuilder builder = mecanumDrive.actionBuilder(getInitialPose());

        ShooterActions shooterActions = new ShooterActions(outtakeDrive, kicker);

        CollectRowActions collect = new CollectRowActions(builder, intakeDrive);

        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        autoActionName("MoveToShoot"),
                        builder.splineTo(new Vector2d(shootX, shootY), angle).build(),
                        autoActionName("shoot"),
                        shooterActions.setShooterPower(0.8),
                        autoActionName("Kick"),
                        shooterActions.kick(0.3),
                        autoActionName("Turnoff"),
                        shooterActions.setShooterPower(0),
                        autoActionName("GetMoreBalls"),
                        collect.toRowAction(0),
                        autoActionName("MoveToShoot"),
                        builder.splineTo(new Vector2d(shootX, shootY), angle).build(),
                        autoActionName("shoot"),
                        shooterActions.setShooterPower(0.8),
                        autoActionName("Kick"),
                        shooterActions.kick(0.3),
                        autoActionName("Turnoff"),
                        shooterActions.setShooterPower(0.0),
                        autoActionName("ending Auto"),
                        builder.splineTo(new Vector2d(0, -1), 0).build()
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
