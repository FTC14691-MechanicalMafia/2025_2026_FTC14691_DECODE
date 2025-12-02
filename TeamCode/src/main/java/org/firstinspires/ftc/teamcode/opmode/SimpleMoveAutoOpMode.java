package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Simple Move")
public class SimpleMoveAutoOpMode extends RRAutoOpMode {

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

        // Create our sequence of things that we want to do
        runningActions.add(
                new SequentialAction(
                        autoActionName("Simple Move"),
                        builder.strafeTo(new Vector2d(-25, -10)).build(),
                        builder.strafeTo(new Vector2d(-25, -10)).build(),
                        autoActionName("Complete")
                )
        );
    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = -33;
        public int positionY = -62;
        public int heading = 90;
    }

}
