/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake;
import org.iolani.frc.subsystems.Intake.RampPosition;

/**
 *
 * @author iobotics
 */
public class SetIntakeVariablePower extends CommandBase {
 	
	private final double              _max;
	private final Intake.RampPosition _position;
	
	public SetIntakeVariablePower(double maxPower) {
		this(maxPower, null);
	}
	
    public SetIntakeVariablePower(double maxPower, Intake.RampPosition position) {
        requires(intake);
        _max      = maxPower;
        _position = position;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(_position != null) {
    		intake.setRampPosition(_position);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double power = ((oi.getRightStick().getTwist() + 1) / 2) * _max;
        intake.setPower(power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.setPower(0.0);
    	if(_position != null) {
    		intake.setRampPosition(RampPosition.Retracted);
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
