package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    public static double MAX_OUTTAKE_POWER = 0.8;
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
    public double firingSpeed = 0.0; //needs to be changed, is the speed the outtake motor needs to spin
    public double[] AT20_poses = {0.0, 0.0, 0.0};//need to be updated; order: xPose, yPose, heading
    //optional change: public Double[] AT20_poses = Double[3];
    public double[] AT24_poses = {0.0, 0.0, 0.0};//need to be updated; order: xPose, yPose, heading
    //optional change: public Double[] AT24_poses = Double[3];
    public double[] Blue_ballPlaces = {-4.0, 1.0, -4.0, -1.0, -4.0, -3.0}; //(x3,y3), (x2,y2), (x1,y1)
    //any robot at y >= 0 is closer to place3, robot between y = 0 & y = -2 is closer to place2, robot at y <= -2 is closer to place1
    public double[] Red_ballPlaces = {4.0, 1.0, 4.0, -1.0, 4.0, -3.0}; //(x3,y3), (x2,y2), (x1,y1)
    //any robot at y >= 0 is closer to place3, robot between y = 0 & y = -2 is closer to place2, robot at y <= -2 is closer to place1
    //any robot at x<=0 is closer to blue places & at x>0 is closer to red places
    public double[] LargeLaunchZoneBoundingCoords = {-6.0, 6.0, 0.0, 0.0, 6.0, 6.0}; //x1, y1, x2, y2, x3, y3
    public double[] SmallLaunchZoneBoundingCoords = {-2.0, -6.0, 0.0, -4.0, 2.0, -6.0}; //x1, y1, x2, y2, x3, y3
    //any robot at y<0 is closer to small launch zone, y>=0 is closer to large launch zone
    public double[] rangePowers = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    //power needed for given distance, e.g. index0 -> power for 0.5 ft away
    public String team = "blue";
}
