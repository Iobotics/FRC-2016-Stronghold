/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.subsystems.ShooterGimbal;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;

/**
 *
 * @author iobotics
 */
public class OperateGimbalSlow extends CommandBase {
   
	private static final double SPEED_DEG_PER_SEC = 3.5;
	private static final double STEP_SIZE_DEGREES = 1.0;
	
	private double _azimuthCurrent;
	private double _elevationCurrent;
	private double _lastTime;
	private int    _lastPOV;
	
    public OperateGimbalSlow() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_lastTime = this.timeSinceInitialized();
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Shot);
    	_azimuthCurrent   = shooterGimbal.getAzimuthDegrees();
    	_elevationCurrent = shooterGimbal.getElevationDegrees();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double azimuth   = oi.getGunnerStick().getX() * 2;
        double elevation = oi.getGunnerStick().getY();
        
        double deltaTime = (this.timeSinceInitialized() - _lastTime);
        double deltaStep = SPEED_DEG_PER_SEC * deltaTime;
        
        int pov = oi.getGunnerStick().getPOV(); 
        if(pov != _lastPOV) {
        	switch(pov) {
        		case 0:
        			_elevationCurrent -= STEP_SIZE_DEGREES;
        			break;
        		case 180:
        			_elevationCurrent += STEP_SIZE_DEGREES;
        			break;
        	}
        	_lastPOV = pov;
        } else {
        	_azimuthCurrent   += azimuth   * deltaStep;
        	_elevationCurrent += elevation * deltaStep;
        }
        _lastTime = this.timeSinceInitialized();
        
        // enforce limits to prevent run-up //
        if(_azimuthCurrent < ShooterGimbal.AZIMUTH_DEGREES_MIN) {
        	_azimuthCurrent = ShooterGimbal.AZIMUTH_DEGREES_MIN;
        } else if(_azimuthCurrent > ShooterGimbal.AZIMUTH_DEGREES_MAX) {
        	_azimuthCurrent = ShooterGimbal.AZIMUTH_DEGREES_MAX;
        }
        if(_elevationCurrent < ShooterGimbal.ELEVATION_SHOT_DEGREES_MIN) {
        	_elevationCurrent = ShooterGimbal.ELEVATION_SHOT_DEGREES_MIN;
        } else if(_elevationCurrent > ShooterGimbal.ELEVATION_SHOT_DEGREES_MAX) {
        	_elevationCurrent = ShooterGimbal.ELEVATION_SHOT_DEGREES_MAX;
        }
        
        shooterGimbal.setAzimuthSetpointDegrees(_azimuthCurrent);
        shooterGimbal.setElevationSetpointDegrees(_elevationCurrent);
        
        /*double azimuth   = oi.getGunnerStick().getX() * 2;
        double elevation = oi.getGunnerStick().getY();
        
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
        _lastTime = this.timeSinceInitialized();*/
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Full);
    	//shooterGimbal.setAzimuthPower(0.0);
    	//shooterGimbal.setElevationPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
