package org.iolani.frc.commands;

/**
 *
 */
public class SetShooterWheelPower extends CommandBase {
	private final double _power;
	private final boolean _terminate;
    
    public SetShooterWheelPower(double power) {
    	this(power, false);
    }
    
    public SetShooterWheelPower(double power, boolean terminate) {
    	requires(shooterWheels);
    	_power     = power;
    	_terminate = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterWheels.setPower(_power);
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
