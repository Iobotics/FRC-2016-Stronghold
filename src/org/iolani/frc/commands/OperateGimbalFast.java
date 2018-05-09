/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 *
 * @author iobotics
 */
public class OperateGimbalFast extends CommandBase {
   
	/*private static final PowerScaler _scaler = new PowerScaler(new PowerScaler.PowerPoint[] {
            new PowerScaler.PowerPoint(0.0, 0.0),
            new PowerScaler.PowerPoint(0.5, 0.1),
            new PowerScaler.PowerPoint(1.0, 0.4)
        });*/
	
	private static final double DEADBAND = 0.1;
	
	private boolean _azimuthLocked;
	private boolean _elevationLocked; 
	private int     _lastPOV;
	
    public OperateGimbalFast() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Shot);
    	_azimuthLocked = true;
    	shooterGimbal.setAzimuthSetpointDegrees(shooterGimbal.getAzimuthDegrees());
    	_elevationLocked = true;
    	shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	int pov = oi.getGunnerStick().getPOV(); 
        if(pov != _lastPOV) {
        	switch(pov) {
        		case 0: // 
        			shooterGimbal.setElevationSetpointDegrees(39);
        			break;
        		case 90:
        			shooterGimbal.setElevationSetpointDegrees(45);
        			break;
        		case 180:
        			shooterGimbal.setElevationSetpointDegrees(42);
        			break;
        		case 270:
        			shooterGimbal.setElevationSetpointDegrees(52);
        			break;
        	}
        	_lastPOV = pov;
        } else {
        	// azimuth control //
            double azimuth = oi.getGunnerStick().getX(Hand.kLeft);
            if(Math.abs(azimuth) > DEADBAND) {
            	azimuth *= 0.3;
            	shooterGimbal.setAzimuthPower(azimuth);
            	_azimuthLocked = false;
            } else if(!_azimuthLocked){
            	// hold position //
            	shooterGimbal.setAzimuthSetpointDegrees(shooterGimbal.getAzimuthDegrees());
            	_azimuthLocked = true;
            }
            
            // elevation control //
            double elevation = oi.getGunnerStick().getY(Hand.kLeft);
            if(Math.abs(elevation) > DEADBAND) {
            	elevation *= 0.3;
            	shooterGimbal.setElevationPower(elevation);
            	_elevationLocked = false;
            } else if(!_elevationLocked){
            	// hold position //
            	shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees());
            	_elevationLocked = true;
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Full);
    	// hold position //
    	shooterGimbal.setAzimuthSetpointDegrees(shooterGimbal.getAzimuthDegrees());
    	shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
