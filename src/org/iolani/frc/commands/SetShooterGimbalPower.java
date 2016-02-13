package org.iolani.frc.commands;

/**
 *
 */
public class SetShooterGimbalPower extends CommandBase {
	private final double _azimuthPower, _elevationPower;
	private final boolean _terminate;
    
    public SetShooterGimbalPower(double azimuthPower, double elevationPower) {
    	this(azimuthPower, elevationPower, false);
    }
    
    public SetShooterGimbalPower(double azimuthPower, double elevationPower, boolean terminate) {
    	requires(shooterGimbal);
    	_azimuthPower   = azimuthPower;
    	_elevationPower = elevationPower;
    	_terminate      = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setPower(_azimuthPower, _elevationPower);
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
    	shooterGimbal.setPower(0.0, 0.0);
    }
}
