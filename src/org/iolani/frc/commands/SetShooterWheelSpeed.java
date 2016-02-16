package org.iolani.frc.commands;

/**
 *
 */
public class SetShooterWheelSpeed extends CommandBase {
	private final double  _rpm;
	private final boolean _terminate;
    
    public SetShooterWheelSpeed(double rpm) {
    	this(rpm, false);
    }
    
    public SetShooterWheelSpeed(double rpm, boolean terminate) {
    	requires(shooterWheels);
    	_rpm       = rpm;
    	_terminate = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterWheels.setSpeed(_rpm);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//intake.setPower(_leftPower, _rightPower);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _terminate;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	shooterWheels.setPower(0.0);
    }
}
