package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.actions.CollectRowActions;
import org.firstinspires.ftc.teamcode.actions.ShooterActions;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.firstinspires.ftc.teamcode.vision.AprilTagDriver;

import java.util.Optional;

public class AdvancedShootOpMode extends RRAutoOpMode{

    int row = 0; //change as needed

    AprilTagOpMode april = new AprilTagOpMode();
    AprilTagDriver aprilDriver = new AprilTagDriver(telemetry, hardwareMap);

    @Override
    public Pose2d getInitialPose() {
        Optional<AprilTag> tags = aprilDriver.detectAprilTags().stream()
                .filter(aprilTag -> POS_APRIL_TAG_IDS.contains(aprilTag.getId()))
                .findFirst();
        Pose2d orientation = new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
        if(tags.isPresent() && (tags.get().getId() == 20 || tags.get().getId() == 24)){
            orientation = april.aprilLocate(tags.get());
        }
        return orientation;

    }
    double shootX = 0;
    double shootY = 0;
    double angle = 0;
    public static AdvancedShootOpMode.Params PARAMS = new AdvancedShootOpMode.Params();

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

        SleepAction sleepAction = new SleepAction(0.75);

        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        autoActionName("windup"),
                        shooterActions.setShooterPower(0.8),

                        autoActionName("MoveToShoot"),
                        builder.splineTo(new Vector2d(shootX, shootY), angle).build(),

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

                        autoActionName("GetMoreBalls"),
                        collect.toRowAction(row),

                        autoActionName("MoveToShoot"),
                        builder.splineTo(new Vector2d(shootX, shootY), angle).build(),

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
                            shooterActions.setShooterPower(0.0),

                            autoActionName("ending Auto"),
                            builder.splineTo(new Vector2d(0, -1), 0).build()
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
