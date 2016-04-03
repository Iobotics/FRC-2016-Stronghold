package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;

/**
 *
 */
public class AutoDriveTimed extends CommandBase {
	
	public static final double DEFAULT_POWER = 0.35;
	
	private final double _leftPower;
	private final double _rightPower;
		
	
	public AutoDriveTimed(double timeout) {
    	this(timeout, DEFAULT_POWER, DEFAULT_POWER);
    }

	public AutoDriveTimed(double timeout, double power) {
    	this(timeout, power, power);
    }
	
	public AutoDriveTimed(double timeout, double leftPower, double rightPower) {
    	requires(drivetrain);
    	requires(navsensor);
    	_leftPower  = leftPower;
    	_rightPower = rightPower;
    	this.setTimeout(timeout);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	drivetrain.setTank(_leftPower, _rightPower);
    }

	protected void execute() {	
	}
	
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivetrain.setTank(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
