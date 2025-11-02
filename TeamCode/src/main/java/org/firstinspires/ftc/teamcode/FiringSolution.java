package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class FiringSolution {
    public static void calc(AprilTag aprilTag){//advanced calc version
        double Distance = aprilTag.getPose().getX();
        double Height = aprilTag.getPose().getY();
        double angleRate = 0.001;
        double velocityRate = 0.001;
        double minAngle = 0;
        double maxAngle = 45;
        double maxVelocity = 40;
        int b = 0;
        double v = -0.1;
        double a = -32.0; //acceleration due to gravity
            //potential change: start with D = 1 instead of D = 0
            for (double Angle = minAngle; Angle <= maxAngle; Angle+=angleRate) {//angle in degrees
                for (double Velocity = 0; Velocity <= maxVelocity; Velocity += velocityRate) {//velocity in ft/s
                    double x = Math.cos(Math.toRadians(Angle)); //horizontal velocity in ft/s
                    double y = Math.sin(Math.toRadians(Angle)); //vertical velocity in ft/s
                    double z = (-(Velocity * Velocity * y * x) - (Velocity * x * (Math.sqrt((Velocity * Velocity * y * y) - (2 * a * Height))))) / (a);//define the equation and use modulo to decrease specificity
                    double s = z % 0.001;
                    if (Distance == z - s) {
                        if (v != Distance) {
                        /*potential changes:
                        1. D != 0 (no more v to determine the same displacement with multiple different Angle&Velocity)
                        2.
                        */
                            telemetry.addData("Angle: %2f degrees", Angle);
                            telemetry.addData("Velocity: %2f ft/s", Velocity);
                            b = 1;
                            v = Distance;
                        }
                    }
                }
            }
        if (b == 0){
            telemetry.addData("results:", "nothing");
        }
    }
}
