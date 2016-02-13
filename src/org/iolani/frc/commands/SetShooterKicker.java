package org.iolani.frc.commands;

/**
 *
 */
public class SetShooterKicker extends CommandBase {
	private final boolean _value;
	private final boolean _terminate;
    
    public SetShooterKicker(boolean value) {
    	this(value, false);
    }
    
    public SetShooterKicker(boolean value, boolean terminate) {
    	requires(shooterKicker);
    	_value     = value;
    	_terminate = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterKicker.setEnabled(_value);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
    	shooterKicker.setEnabled(false);
    }
}
