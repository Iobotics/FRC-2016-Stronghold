package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake;
import org.iolani.frc.subsystems.Intake.RampPosition;

/**
 *
 */
public class SetIntakePower extends CommandBase {
	
	private final double              _power;
	private final Intake.RampPosition _position;
	private final boolean             _terminate;
  
    public SetIntakePower(double power) {
    	this(power, null, false);
    }
    
    public SetIntakePower(double power, Intake.RampPosition position) {
    	this(power, position, false);
    }
    
    public SetIntakePower(double power, Intake.RampPosition position, boolean terminate) {
    	requires(intake);
    	_power     = power;
    	_position  = position;
    	_terminate = terminate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	intake.setPower(_power);
    	if(_position != null) {
    		intake.setRampPosition(_position);
    	}
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
    	intake.setPower(0.0);
    	if(_position != null) {
    		intake.setRampPosition(RampPosition.Retracted);
    	}
    }
}
