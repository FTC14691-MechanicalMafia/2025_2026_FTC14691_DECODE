package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LimitDrive;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class MM14691BaseOpMode extends OpMode {

    private static final Logger LOG = LoggerFactory.getLogger(MM14691BaseOpMode.class);

    // See https://rr.brott.dev/docs/v1-0/guides/teleop-actions/ for documentation
    protected FtcDashboard dash = FtcDashboard.getInstance();
    protected List<Action> runningActions = new ArrayList<>();
    protected MecanumDrive mecanumDrive = null;
    protected WristDrive wristDrive = null;
    protected IntakeDrive intakeDrive = null;
    protected ViperDrive viperDrive = null;
    protected LimitDrive viperLimitDrive = null;
    protected LiftDrive liftDrive = null;
    protected LimitDrive liftLimitDrive = null;
    // Time tracking
    protected ElapsedTime runtime = new ElapsedTime();

    public abstract Pose2d getInitialPose();

    @Override
    public void init() {
        LOG.info("Init: start");

        // Start the Mechanum Drive
        mecanumDrive = new MecanumDrive(hardwareMap, getInitialPose());
        telemetry.addData("Mecanum Drive", PoseStorage.currentPose == null ? "Initialized" : "Restored");

        // Start our Arm Drives
        viperDrive = new ViperDrive(hardwareMap, "armViper", gamepad2.right_stick_button);
        telemetry.addData("Viper Drive", viperDrive.getStatus());
        viperLimitDrive = new LimitDrive(hardwareMap.get("viperLimit"));
        telemetry.addData("Viper Start Limit", viperLimitDrive.getStatus());

        liftDrive = new LiftDrive(hardwareMap, "armLift", gamepad2.left_stick_button);
        liftDrive.setViperDrive(viperDrive);
        telemetry.addData("Lift Drive", liftDrive.getStatus());
        liftLimitDrive = new LimitDrive(hardwareMap.get("liftLimit"));
        telemetry.addData("Lift Start Limit", liftLimitDrive.getStatus());

        intakeDrive = new IntakeDrive(hardwareMap, "intake");
        telemetry.addData("Intake Drive", intakeDrive.getStatus());

        wristDrive = new WristDrive(hardwareMap, "wrist");
        wristDrive.setIntakeDrive(intakeDrive); // they interact with each other to prevent crashing
        telemetry.addData("Wrist Drive", wristDrive.getStatus());

        // Refresh the driver screen
        telemetry.update();

        LOG.info("Init: Complete");
    }

    @Override
    public void start() {
        super.start();

        LOG.info("Start: start");

        TelemetryPacket packet = new TelemetryPacket();

        // restarts runtime so the time starts when the play button is pushed
        runtime.reset();

        // Update the values from the poinpoint hardware
        mecanumDrive.updatePoseEstimate();
        telemetry.addData("Mecanum Drive", "Ready");

        //Add our debugging action
        runningActions.add(new DebugAction());

        //Add the limits for the viper drive
        runningActions.add(viperDrive.limits());
        telemetry.addData("Viper Drive", "Ready");
        viperLimitDrive.addListener(viperDrive.startLimitListener());
        runningActions.add(viperLimitDrive.watchLimit());
        telemetry.addData("Viper Start Limit", viperLimitDrive.getStatus());

        //Add the limit checks to the lift drive
        runningActions.add(liftDrive.limits());
        //Allow the lift drive to dynamically update the viper limit
        runningActions.add(liftDrive.adjustViperLimits());
        telemetry.addData("Lift Drive", "Ready");
        liftLimitDrive.addListener(liftDrive.startLimitListener());
        runningActions.add(liftLimitDrive.watchLimit());
        telemetry.addData("Lift Start Limit", liftLimitDrive.getStatus());

        //Prepare the wrist for intake
        telemetry.addData("Wrist Drive", "Ready");

        // Start the intake if needed
        runningActions.add(intakeDrive.toClosed());
        telemetry.addData("Intake Drive", "Ready");

        // Run our actions before we start the loop
        updateRunningActions(packet);

        // Refresh the driver screen
        telemetry.update();

        dash.sendTelemetryPacket(packet);

        LOG.info("Start: Complete");
    }

    protected void updateRunningActions(TelemetryPacket packet) {
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            } else {
                LOG.info("Action complete: {}", action);
            }
        }
        runningActions = newActions;

        // Update all the telemetries
        telemetry.addData("Mecanum Drive", "Running");
        telemetry.addData("Wrist Drive", wristDrive.getStatus());
        telemetry.addData("Viper Drive", viperDrive.getStatus());
        telemetry.addData("Viper Start Limit", viperLimitDrive.getStatus());
        telemetry.addData("Lift Drive", liftDrive.getStatus());
        telemetry.addData("Lift Start Limit", liftLimitDrive.getStatus());
        telemetry.addData("Intake Drive", intakeDrive.getStatus());
    }

    @Override
    public void stop() {
        super.stop();

        LOG.info("Stop: starting");

        // Clear our running actions, just in case
        runningActions.clear();

        telemetry.addData("Mecanum Drive", "Stopping");
        telemetry.addData("Wrist Drive", "Stopping");
        telemetry.addData("Viper Drive", "Stopping");
        telemetry.addData("Viper Start Limit", "Stopping");
        telemetry.addData("Lift Drive", "Stopping");
        telemetry.addData("Lift Start Limit", "Stopping");
        telemetry.addData("Intake Drive", "Stopping");

        // Refresh the driver screen
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();

        LOG.info("Stop: complete");
    }

    public class DebugAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetry.addData("MecanumDrive",
                    "x (%f), y (%f), h (%f)",
                    mecanumDrive.pose.position.x,
                    mecanumDrive.pose.position.y,
                    mecanumDrive.pose.heading.real);

            viperDrive.addDebug(telemetry);
            liftDrive.addDebug(telemetry);
            wristDrive.addDebug(telemetry);
            intakeDrive.addDebug(telemetry);

            telemetry.addData("Runtime", runtime.seconds());

            return true; // Always run this so we always emit debug info
        }

    }

}
