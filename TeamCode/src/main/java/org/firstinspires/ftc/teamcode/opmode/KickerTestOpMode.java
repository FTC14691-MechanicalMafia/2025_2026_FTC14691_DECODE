package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class KickerTestOpMode extends MMDriveOpMode{
    @Override
    public Pose2d getInitialPose() {
        // Return 0s since we don't care about our position in TeleOp.
        return new Pose2d(new Vector2d(0, 0), 0);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_right){
            kicker.setPosition((kicker.getPosition())+0.2);
        } else if (gamepad1.dpad_left){
            kicker.setPosition((kicker.getPosition())-0.2);
        }
    }
}
