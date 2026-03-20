package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.opmode.AprilTagOpMode;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.firstinspires.ftc.teamcode.vision.AprilTagDriver;

import java.util.List;
import java.util.Optional;

@Autonomous(name="Simple Move")
public class StartFromBackAutoOpMode extends RRAutoOpMode {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();
    final RobotConstants constants = new RobotConstants();
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

    @Override
    public void start() {
        TrajectoryActionBuilder builder = mecanumDrive.actionBuilder(getInitialPose());
        /*int lap = 0;
        while (time <= 20) {

            super.start();

            // Get our trajectory builder to add actions to
            // Create our sequence of things that we want to do
            super.fire(tags.get());
            if (lap % 3 == 0) {
                runningActions.add(
                        new SequentialAction(
                                autoActionName("MoveToNewAmmo"),
                                builder.splineTo(new Vector2d(placesCoords[4 - lap / 3], placesCoords[5 - lap / 3]), 90).build()

                        )
                );
            }
            if (lap % 3 == 1) {
                super.intake(1);
                runningActions.add(
                        new SequentialAction(
                                builder.lineToX(placesCoords[8 - lap / 3]).build(),
                                autoActionName("NewAmmoAcquired")
                        )
                );
                super.intake(0);
            }
            if (lap % 3 == 2){
                runningActions.add(
                        new SequentialAction(
                                builder.splineTo(new Vector2d(placesCoords[9], placesCoords[10]), placesCoords[11]).build(),
                                autoActionName("TargetAcquired")
                        )
                );
                super.fire(tags.get());
            }
            lap++;
        }
        runningActions.add(
                /*new SequentialAction(
                        builder.splineTo(new Vector2d(placesCoords[12], placesCoords[13]), 0).build(),
                        autoActionName("ReadyToEnd")
                )
                new SequentialAction(
                        autoActionName("Move off start"),
                        builder.lineToX(10).build()
                )
        );*/
        runningActions.add(
                new SequentialAction(
                        autoActionName("Move off start"),
                        builder.lineToX(10).build()
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
