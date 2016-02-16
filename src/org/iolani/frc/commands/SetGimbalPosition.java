/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.util.PowerScaler;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class SetGimbalPosition extends CommandBase {
	
	private static final double _errorDegrees = 1.0;
	private final double _azimuth;
	private final double _elevation;
	
    public SetGimbalPosition(double azimuthDegrees, double elevationDegrees) {
        requires(shooterGimbal);
        _azimuth   = azimuthDegrees;
        _elevation = elevationDegrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setAzimuthSetpointDegrees(_azimuth);
    	shooterGimbal.setElevationSetpointDegrees(_elevation);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean azimuthOnTarget = Math.abs(shooterGimbal.getAzimuthErrorDegrees()) < _errorDegrees;
    	boolean elevationOnTarget = Math.abs(shooterGimbal.getElevationErrorDegrees()) < _errorDegrees;
        return azimuthOnTarget && elevationOnTarget;
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
