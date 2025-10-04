package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.AprilTags;
@TeleOp(name="Shooting", group="Linear OpMode")

public class Shooting extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor shootingDrive = null;

    @Override
    public void runOpMode(){
        waitForStart();
        shootingDrive = hardwareMap.get(DcMotor.class, "shooting_drive");
        if (opModeIsActive()) {
            while (opModeIsActive()) {

            }
        }
    }
}
