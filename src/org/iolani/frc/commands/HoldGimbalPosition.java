/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class HoldGimbalPosition extends CommandBase {
	
    public HoldGimbalPosition() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double azimuth   = shooterGimbal.getAzimuthDegrees();
    	double elevation = shooterGimbal.getElevationDegrees();
    	shooterGimbal.setAzimuthSetpointDegrees(azimuth);
    	shooterGimbal.setElevationSetpointDegrees(elevation);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setAzimuthPower(0.0);
    	shooterGimbal.setElevationPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
