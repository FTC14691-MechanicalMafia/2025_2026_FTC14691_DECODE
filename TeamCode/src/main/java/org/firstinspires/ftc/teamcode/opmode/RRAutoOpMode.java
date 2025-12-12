package org.firstinspires.ftc.teamcode.opmode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.vision.AprilTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Auto Op Mode that provides RoadRunner as a base.
 */
public abstract class RRAutoOpMode extends MMDriveOpMode {

    private static final Logger LOG = LoggerFactory.getLogger(RRAutoOpMode.class);

    // See https://rr.brott.dev/docs/v1-0/guides/teleop-actions/ for documentation
    protected FtcDashboard dash = FtcDashboard.getInstance();
    protected List<Action> runningActions = new ArrayList<>();

    private String runningAction = "";

    public String getRunningAction() {
        return runningAction;
    }

    @Override
    public void init() {
        super.init();

        telemetry.addData("Running Action: %s", this::getRunningAction);
    }

    @Override
    public void loop() {
        super.loop();

        // Requirements for running roadrunner actions
        TelemetryPacket packet = new TelemetryPacket();

        // see if we have an april tag in sight
        List<AprilTag> tags = new ArrayList<>();
        if (RobotConstants.CAMERA_ENABLED) {
            // Get any april tags that are visible this loop
            tags = aprilTagDrive.detectAprilTags();
            this.aprilTagStatus = tags.stream()
                    // sort by the id values
                    .sorted(Comparator.comparing(AprilTag::getId))
                    // convert the april tag to its ID as a string
                    .map(aprilTag -> String.valueOf(aprilTag.getId()))
                    // join the ids together separated by a comma
                    .collect(Collectors.joining(","));
        }

        // update our position based based on the first detected vision pose
        Optional<AprilTag> optionalAprilTag = tags.stream()
                .filter(aprilTag -> POS_APRIL_TAG_IDS.contains(aprilTag.getId()))
                .findFirst();
        if (optionalAprilTag.isPresent()) {
            //TODO - the calculation to the field position
            //take apriltag heading & distance to calculate relative location
            //take the april tags known position on the field and relative location for robot to calculate robots location
            //pinpointDrive.setPinpointPosition();
        }

        // update the pose estimate on the drive since we actually care about our position info
        mecanumDrive.updatePoseEstimate();

        // Update the actions we care about
        updateRunningActions(packet);

        telemetry.update();

        // Send the packat
        dash.sendTelemetryPacket(packet);
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
    }


    /**
     * This is for a readout on the driver station about which part of the auto mode we are currently running.
     * It basically just records whatever name to the screen
     */
    public class AutoActionName implements Action {
        private String name;

        public AutoActionName(String name) {
            this.name = name;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            LOG.info("Starting Auto Action: {}", this.name);
            runningAction = this.name;
            return false; // we don't want this to continue running
        }
    }

    public AutoActionName autoActionName(String name) {
        return new AutoActionName(name);
    }
}
