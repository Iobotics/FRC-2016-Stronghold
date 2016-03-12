/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class OperateGimbalManual extends CommandBase {
   
	private static final double AZIMUTH_DEGREES_MAX = 35;
	private static final double ELEVATION_DEGREES_MIN = 25;
	private static final double ELEVATION_DEGREES_MAX = 60;
	
    public OperateGimbalManual() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
    	double azimuth = AZIMUTH_DEGREES_MAX * oi.getGunnerStick().getX(Hand.kRight);
    	shooterGimbal.setAzimuthSetpointDegrees(azimuth);
    	
    	double elevation = ELEVATION_DEGREES_MIN + (1 - oi.getGunnerStick().getY(Hand.kRight))
    			* (ELEVATION_DEGREES_MAX - ELEVATION_DEGREES_MIN) / 2;
    	shooterGimbal.setElevationSetpointDegrees(elevation);
    	
    	SmartDashboard.putNumber("gimbal-azimuth-command", azimuth);
    	SmartDashboard.putNumber("gimbal-elevation-command", elevation);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
