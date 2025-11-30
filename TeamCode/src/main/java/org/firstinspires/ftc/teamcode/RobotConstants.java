package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    /**
     * When true, the intake motor is expected to be present on the robot.  If false, the motor will not be
     * initialized and intake is unavailable.
     */
    public static Boolean INTAKE_ENABLED = true;

    /**
     * When true, the outtake motor and servos are expected to be present on the robot.  If false, the motor and servos will not be
     * initialized and intake is unavailable.
     */
    public static Boolean OUTTAKE_ENABLED = true;

    /**
     * For the out take aiming servo, what position should it move to?
     * Note: This will also be the base position that other positions are calculated off of
     */
    public static double OUTTAKE_AIM_INIT_POS = 0;
//    public static double OUTTAKE_AIM_MIN_POS = 0;
//    public static double OUTTAKE_AIM_MAX_POS = 0.1;
    public static double OUTAKE_SHOOT_LAUNCH_POS = .5;
    public static double OUTTAKE_SHOOT_REST_POS = 0;


    /**
     * When true, the Drive motors are expected to be present on the robot.  If false, the motors will not be
     * initialized and intake is unavailable.
     */
    public static Boolean DRIVE_ENABLED = true;

    /**
     * Is odometry enabled?
     */
    public static Boolean ODO_ENABLED = true;

    /**
     * When true, the camera is expected to be present on the robot.  If false, the camera will not be
     * initialized and intake is unavailable.
     */
    public static Boolean CAMERA_ENABLED = true;

}
