package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@TeleOp(name = "ShootTest Mode", group = "Competition")
public class ShootTestOpMode extends MMDriveOpMode {
    AprilTagOpMode april = new AprilTagOpMode();
    boolean intakeOn = false;
    boolean outtakeOn = false;
    @Override
    public Pose2d getInitialPose() {
        // Return 0s since we don't care about our position in TeleOp.
        return new Pose2d(new Vector2d(0, 0), 0);
    }

    public void loop() {
        // Do the stuff our parent class set up
        super.loop();
        // Controller 1
        // implement left stick for mechanic forward/strafe

        // implement right stick for rotation and the auto aiming features

        double distance = 0;
        if (RobotConstants.CAMERA_ENABLED) {
            // Get any april tags that are visible this loop
            List<AprilTag> tags = aprilTagDrive.detectAprilTags();
            this.aprilTagStatus = tags.stream()
                    // sort by the id values
                    .sorted(Comparator.comparing(AprilTag::getId))
                    // convert the april tag to its ID as a string
                    .map(aprilTag -> String.valueOf(aprilTag.getId()))
                    // join the ids together separated by a comma
                    .collect(Collectors.joining(","));

            // this is to prevent oscillating back and forth due to overshoot (I think?)
            int id = 0;
            if(tags.contains(20)){
                id = 20;
            }else if(tags.contains(24)){
                id = 24;
            }
            distance = tags.get(id).getTargetting().getRange();
            if(tags.get(id).getTargetting().getBearing() < 5.0 && tags.get(id).getTargetting().getBearing() > -5.0){
                telemetry.addLine("on target");
            }
            telemetry.addData("distance from april tag: %.2f", distance);
            // TODO - recalibrate the IMU to the position based on the april tag.
        }



        // Controller 2

        // left trigger for intake on/off
        if (RobotConstants.INTAKE_ENABLED) {
            if(gamepad2.left_trigger > 0 && !intakeOn) intakeOn = true;
            if(gamepad2.left_trigger > 0 && intakeOn) intakeOn = false;
            double intakePower = gamepad2.left_trigger > 0.5 ? 1.0 : 0.0;
            intakeDrive.setPower(intakePower);
            this.intakeStatus = "Pow: " + intakePower;
        }
        double outtakePower = 0.0;
        // right trigger for outtake on/off
        if (RobotConstants.OUTTAKE_ENABLED) {
            if(gamepad2.right_trigger > 0 && !outtakeOn) outtakeOn = true;
            if(gamepad2.right_trigger > 0 && outtakeOn) outtakeOn = false;
            if (gamepad2.dpad_up) outtakePower = 0.0;
            if (gamepad2.dpad_down) outtakePower = 0.0;
            if (gamepad2.dpad_left) outtakePower -= 0.1;
            if (gamepad2.dpad_right) outtakePower += 0.1;
            outtakeDrive.setPower(outtakePower);


            //  right stick up/down aiming for distance angle (hood angle) - servo
            if (gamepad2.right_stick_y != 0) {
                double aimpos = gamepad2.right_stick_y;
                aimpos = aimpos + 1;
                aimpos = aimpos / 2;
                aimpos = (aimpos * .2) + .1 ;

                if (aimpos > .3) aimpos = .3;
                if (aimpos < .1) aimpos = .1;
                aimServo.setPosition(aimpos);
            }

            //  RT for shoot ball - servo
            if (gamepad2.right_trigger > 0) {
                shootServo.setPower(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS);
            } else {
                shootServo.setPower(RobotConstants.OUTTAKE_SHOOT_REST_POS);
            }

            this.outtakeStatus = String.format("Pow: %.3f, Aim: %.3f, Shoot: %.3f",
                    outtakeDrive.getPower(), aimServo.getPosition(), shootServo.getPower());
        }
        // update telemetry
        telemetry.update();
    }

}
//TODO - calculate in speed rather than power
