package org.iolani.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
    // drive //
    public static final int driveLeftMain      = 1;
    public static final int driveLeftSlave1    = 2;
    public static final int driveLeftSlave2    = 3;
    public static final int driveRightMain     = 4;
    public static final int driveRightSlave1   = 5;
    public static final int driveRightSlave2   = 6;
    public static final int driveLeftEncoderA  = 3;
    public static final int driveLeftEncoderB  = 4;
    public static final int driveRightEncoderA = 5;
    public static final int driveRightEncoderB = 6;
    
    // intake //
    public static final int intakeTalon = 9;
    public static final int intakeValve = 7;
    
    // shooter //
    public static final int shooterAzimuth      = 10;
    public static final int shooterElevation    = 7;
    public static final int shooterWheelLeft    = 12;
    public static final int shooterWheelRight   = 11;
    public static final int shooterKickValve    = 0;
    public static final int shooterAzimuthADC   = 0;
    public static final int shooterElevationADC = 1;
    public static final int shooterDebugDIO     = 9;
    
    // camera //
    public static final int cameraServoPWM = 0;
}
