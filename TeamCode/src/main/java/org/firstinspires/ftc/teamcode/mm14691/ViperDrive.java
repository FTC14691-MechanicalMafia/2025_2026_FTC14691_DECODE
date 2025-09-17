package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MotorDrive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Config
public class ViperDrive extends MotorDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the viper motor move from the limit switch
         */
        public int endLimit = 2360;

        public int liftUpLimit = 3300;
        /**
         * Allow overriding the limit from the console.
         */
        public boolean startLimitActive = true;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean endLimitActive = true;

        /**
         * Store the last run's start limit
         */
        public static int lastRunStartLimit = 0;

        public boolean debugEnabled = true;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ViperDrive.class);

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    public ViperDrive(HardwareMap hardwareMap, String motorName, boolean useLastRunStartLimit) {
        this(hardwareMap.get(DcMotorEx.class, motorName));

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        //in case the motor is REVERSE, the start and end will have already been read wrong.  Reset them
        setStartTick(motor.getCurrentPosition());
        setEndTick(motor.getCurrentPosition() + PARAMS.endLimit);
    }

    public ViperDrive(DcMotorEx motor) {
        // set this to wherever the viper is currently resting.  This will be reset when we hit the limit switch.
        super(motor,
                motor.getCurrentPosition(),
                motor.getCurrentPosition() + PARAMS.endLimit);

        setStartLimitEnabled(PARAMS.startLimitActive);
        setEndLimitEnabled(PARAMS.endLimitActive);

        setDebugEnabled(PARAMS.debugEnabled);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
