package org.iolani.frc.commands;

/**
 *
 */
public class SetGimbalPower extends CommandBase {
	private final double _azimuthPower, _elevationPower;
	private final boolean _terminate;
    
    public SetGimbalPower(double azimuthPower, double elevationPower) {
    	this(azimuthPower, elevationPower, false);
    }
    
    public SetGimbalPower(double azimuthPower, double elevationPower, boolean terminate) {
    	requires(shooterGimbal);
    	_azimuthPower   = azimuthPower;
    	_elevationPower = elevationPower;
    	_terminate      = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setAzimuthPower(_azimuthPower);
    	shooterGimbal.setElevationPower(_elevationPower);
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
    	shooterGimbal.setAzimuthPower(0);
    	shooterGimbal.setElevationPower(0);
    }
}
