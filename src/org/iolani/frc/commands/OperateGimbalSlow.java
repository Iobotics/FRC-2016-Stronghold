/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 *
 * @author iobotics
 */
public class OperateGimbalSlow extends CommandBase {
   
	private static final double SPEED_DEG_PER_SEC = 10;
	
	private double _lastTime;
	private int    _lastPOV;
	
    public OperateGimbalSlow() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_lastTime = this.timeSinceInitialized();
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Shot);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double azimuth   = oi.getGunnerStick().getX(Hand.kLeft) * 2;
        double elevation = oi.getGunnerStick().getY(Hand.kLeft);
        
        //shooterGimbal.setAzimuthSpeed(azimuth * SPEED_DEG_PER_SEC);
        //shooterGimbal.setElevationSpeed(elevation * SPEED_DEG_PER_SEC);
        
        double deltaTime = (this.timeSinceInitialized() - _lastTime);
        double deltaStep = SPEED_DEG_PER_SEC * deltaTime;
        shooterGimbal.setAzimuthSetpointDegrees(shooterGimbal.getAzimuthDegrees() + azimuth * deltaStep);
        shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees() + elevation * deltaStep);
        
        int pov = oi.getGunnerStick().getPOV(); 
        if(pov != _lastPOV) {
        	switch(pov) {
        		case 0:
        			shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees() - 1);
        			break;
        		case 180:
        			shooterGimbal.setElevationSetpointDegrees(shooterGimbal.getElevationDegrees() + 1);
        			break;
        	}
        	_lastPOV = pov;
        }
        _lastTime = this.timeSinceInitialized();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Full);
    	shooterGimbal.setAzimuthPower(0.0);
    	shooterGimbal.setElevationPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
