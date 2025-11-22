package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class PositionOffsetTuner extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SparkFunOTOSDrive sparkFunOTOSDrive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0,0,0));
        telemetry.addLine("OTOS Position Offset Tuner");
        telemetry.addLine("Line the robot against the corner of two walls facing forward and Press START.");
        telemetry.addLine("Then rotate the robot exactly 180 degrees and press it back into the corner.");
        telemetry.addLine("Finally, copy the pose offset into line 38 of SparkFunOTOSDrive.");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            sparkFunOTOSDrive.updatePoseEstimate();
            telemetry.addData("Heading (deg)",Math.toDegrees(sparkFunOTOSDrive.pose.heading.toDouble()));
            if (Math.abs(Math.toDegrees(sparkFunOTOSDrive.pose.heading.toDouble())) > 175) {
                telemetry.addData("X Offset", sparkFunOTOSDrive.pose.position.x / 2);
                telemetry.addData("Y Offset", sparkFunOTOSDrive.pose.position.y / 2);
            } else {
                telemetry.addLine("Rotate the robot 180 degrees and align it to the corner again.");
            }
            telemetry.update();
        }


    }
}