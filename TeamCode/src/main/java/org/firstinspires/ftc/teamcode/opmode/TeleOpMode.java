package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp
public class TeleOpMode extends MMDriveOpMode {

    public void loop() {
        // Get any april tags that are visible this loop
        if (RobotConstants.CAMERA_ENABLED) {
            List<AprilTag> tags = aprilTagDrive.detectAprilTags();
        }


        // Controller 1
        // implement left stick for mechanic forward/strafe
        double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value


        //finds whether direction is pos or neg then multiplies by the adjusted value
        double lateral = (gamepad1.left_stick_x);

        // implement right stick for rotation
        double autoAimPower = 0;
        if (RobotConstants.CAMERA_ENABLED) {
            autoAimPower = autoAim();
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

        //to create adjustment curve, use Math.pow(2, gamepad#.stick - 1);
        //if yaw = 0, give the all clear to shoot
        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;
        double multiplier;
        if (gamepad1.left_trigger < 0) {
            //  LT for slow
            multiplier = 0.5;
        } else if (gamepad1.right_trigger < 0) {
            //  RT for boost
            multiplier = 1;
        }else {
            multiplier = 0.75;
        }
        frontLeftPower = (axial + lateral + yaw) * multiplier;
        frontRightPower = (axial - lateral - yaw) * multiplier;
        backLeftPower = (axial - lateral + yaw) * multiplier;
        backRightPower = (axial + lateral - yaw) * multiplier;

        /*
        double absPower = Math.min(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        absPower =  Math.min(absPower, Math.abs(backLeftPower));
        absPower = Math.min(absPower, Math.abs(backRightPower));
        frontLeftPower = frontLeftPower/Math.abs(frontLeftPower) * absPower;
        frontRightPower = frontRightPower/Math.abs(frontRightPower) * absPower;
        backLeftPower = backLeftPower/Math.abs(backLeftPower) * absPower;
        backRightPower = backRightPower/Math.abs(backRightPower) * absPower;
        */
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
            intakeDrive.setPower(gamepad2.left_trigger > 0.5 ? 1.0 : 0.0);
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


        }

        // TODO - update the telemetry
        if(true){//replace with a check of whether Mecanum works
            telemetry.addLine("Mecanum: Online");
        } else {
            telemetry.addLine("Mecanum: Offline");
        }        if(true){//replace with a check of whether pinpoint works
            telemetry.addLine("Pinpoint: Online");
        } else {
            telemetry.addLine("Pinpoint: Offline");
        }        if(true){//replace with a check of whether camera works
            telemetry.addLine("Camera: Online");
        } else {
            telemetry.addLine("Camera: Offline");
        }
        if(true){//replace with a check of whether Color sensor works
            telemetry.addLine("Color: Online");
        } else {
            telemetry.addLine("Color: Offline");
        }
        if (RobotConstants.INTAKE_ENABLED) {
            telemetry.addLine("Intake: " + status(intakeDrive));
        }
        if (RobotConstants.OUTTAKE_ENABLED) {
            telemetry.addLine("Outtake: " + status(outtakeDrive));
        }
        telemetry.addLine("Distance: Offline");

        telemetry.addLine(toString().valueOf(autoAimPower));

        // Check for april tags
        if (RobotConstants.CAMERA_ENABLED) {
            final List<AprilTag> aprilTags = aprilTagDrive.detectAprilTags();
            telemetry.addLine("AT IDs: " +
                    aprilTags.stream()
                            // sort by the id values
                            .sorted(Comparator.comparing(AprilTag::getId))
                            // convert the april tag to its ID as a string
                            .map(aprilTag -> String.valueOf(aprilTag.getId()))
                            // join the ids together separated by a comma
                            .collect(Collectors.joining(","))
            );
            // print the tags we care about to telemetry
            if(gamepad2.a) {
                //allows pilots to control whether data is presented
                //doesnt need to be pad 2 button a
                aprilTags.stream()
                        // filter the stream to the tags we want
                        .filter(aprilTag -> APRIL_TAG_IDS.contains(aprilTag.getId()))
                        .forEach(aprilTag -> {
                            telemetry.addData(aprilTag.getId() + " X pose is " + aprilTag.getPose().getX(), "inches");
                            telemetry.addData(aprilTag.getId() + " Y pose is " + aprilTag.getPose().getY(), "inches");
                            telemetry.addData(aprilTag.getId() + " Z pose is " + aprilTag.getPose().getX(), "inches");
                            telemetry.addData(aprilTag.getId() + " Pitch is " + aprilTag.getRotation().getPitch(), "degrees");
                            telemetry.addData(aprilTag.getId() + " Roll is " + aprilTag.getRotation().getRoll(), "degrees");
                            telemetry.addData(aprilTag.getId() + " Yaw is " + aprilTag.getRotation().getYaw(), "degrees");
                            telemetry.addData(aprilTag.getId() + " Range is " + aprilTag.getTargetting().getRange(), "inches");
                            telemetry.addData(aprilTag.getId() + " Bearing is " + aprilTag.getTargetting().getBearing(), "degrees");
                            telemetry.addData(aprilTag.getId() + " Elevation is " + aprilTag.getTargetting().getElevation(), "inches");
                        });
            }
        }

        // update telemetry
        telemetry.update();
    }
    public String status(DcMotor motor){
        String stat = "Offline";
        if(motor.getPower() > 0){
            stat = "Online";
        }
        return stat;
    }
}
