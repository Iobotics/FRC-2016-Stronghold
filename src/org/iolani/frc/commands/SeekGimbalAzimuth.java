/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class SeekGimbalAzimuth extends CommandBase {
	
	private static final double ERROR_DEGREES = 0.5;
	private static final double CONVERGE_TIME = 0;
	private final double _azimuth;
	
	private enum State {
		Start,
		Seeking,
		WaitForConverge,
		Done
	}
	private State _state;
	private double _waitTime;
	
    public SeekGimbalAzimuth(double azimuthDegrees) {
        requires(shooterGimbal);
        _azimuth = azimuthDegrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setAzimuthSetpointDegrees(_azimuth);
    	_state = State.Start;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double error = shooterGimbal.getAzimuthErrorDegrees();
    	SmartDashboard.putNumber("azimuth-error", error);
    	
    	switch(_state) {
    		case Start:
    			_state = (Math.abs(error) < ERROR_DEGREES)? State.Done: State.Seeking;
    			break;
    		case Seeking:
    	    	if(Math.abs(error) < ERROR_DEGREES) {
    	    		_state = State.WaitForConverge;
    	    		_waitTime = this.timeSinceInitialized() + CONVERGE_TIME;
    	    	}
    	    	break;
    		case WaitForConverge:
    			if(Math.abs(error) >= ERROR_DEGREES) {
    				_state = State.Seeking;
    			}
    			if(this.timeSinceInitialized() >= _waitTime) {
    				_state = State.Done;
    			}
    			break;
    		case Done:
    			break;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _state == State.Done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setAzimuthPower(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
