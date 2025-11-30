package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp(name = "TeleOp Mode", group = "Competition")
public class TeleOpMode extends MMDriveOpMode {

    public void loop() {
        // Controller 1
        // implement left stick for mechanic forward/strafe
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value

        //finds whether direction is pos or neg then multiplies by the adjusted value
        double lateral = (gamepad1.left_stick_x);

        // implement right stick for rotation
        double autoAimPower = 0;
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
            autoAimPower = autoAim(tags);
        }
        double yaw = (gamepad1.right_stick_x);
        if (RobotConstants.CAMERA_ENABLED) {
            if (yaw != 0) {
                yaw = (yaw / Math.abs(yaw)) * Math.pow(Math.abs(yaw), 2);
            } else {
                yaw = 0;
            }
            if (gamepad2.x && Math.abs(autoAimPower) > 0) { // check if autoaim is pushed and we have something to aim at
                yaw = autoAimPower;
            }
        }
        this.driveStatus = String.format("ax: %d, lat: %d, yaw: %d", axial, lateral, yaw);

        //to create adjustment curve, use Math.pow(2, gamepad#.stick - 1);
        //if yaw = 0, give the all clear to shoot
        double multiplier;
        if (gamepad1.left_trigger < 0) {
            //  LT for slow
            multiplier = 0.5;
        } else if (gamepad1.right_trigger < 0) {
            //  RT for boost
            multiplier = 1;
        } else {
            multiplier = 0.75;
        }
        double frontLeftPower = (axial + lateral + yaw) * multiplier;
        double frontRightPower = (axial - lateral - yaw) * multiplier;
        double backLeftPower = (axial - lateral + yaw) * multiplier;
        double backRightPower = (axial + lateral - yaw) * multiplier;

        // set the drive power
        if (RobotConstants.DRIVE_ENABLED) {
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);
        }

        // Controller 2

        // left trigger for intake on/off
        if (RobotConstants.INTAKE_ENABLED) {
            double intakePower = gamepad2.left_trigger > 0.5 ? 1.0 : 0.0;
            intakeDrive.setPower(intakePower);
            this.intakeStatus = "Pow: " + intakePower;
        }

        // right trigger for outtake on/off
        if (RobotConstants.OUTTAKE_ENABLED) {
            // Dpad left (slower) right (faster) outtake motor speed
            if (gamepad2.dpad_left) {
                outtakeDrive.setPower(outtakeDrive.getPower() - .1);
            }
            if (gamepad2.dpad_right) {
                outtakeDrive.setPower(outtakeDrive.getPower() + .1);
            }
            //  right stick up/down aiming for distance angle (hood angle) - servo
            if (gamepad2.right_stick_y != 0) {
                // if the stick is at -1 (bottom) assume that is the start position
                // if the stick is at 1 (top) assume that it should be at the max position
                // using exponential regression
                // TODO - calculate the exponential regression on init based on the configured variables
                aimServo.setPosition(0.1869 * Math.pow(5.35521, gamepad2.right_stick_y));
            }

            //  RT for shoot ball - servo
            if (gamepad2.right_trigger > 0) {
                shootServo.setPosition(RobotConstants.OUTAKE_SHOOT_LAUNCH_POS);
            } else {
                shootServo.setPosition(RobotConstants.OUTTAKE_SHOOT_REST_POS);
            }

            this.outtakeStatus = String.format("Pow: %d, Aim: %d, Shoot: %d",
                    outtakeDrive.getPower(), aimServo.getPosition(), shootServo.getPosition());
        }

        // update telemetry
        telemetry.update();
    }

}
