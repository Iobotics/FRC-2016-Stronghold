package org.iolani.frc.commands;

import org.iolani.frc.commands.CommandBase;

/**
 *  When in outake mode, wait for a button press to launch the ball for "grounder" shooting.
 */
public class OperateOutakeBallEject extends CommandBase {

    public OperateOutakeBallEject() {
    	requires(shooterWheels);
    	requires(shooterKicker);
    }

    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(oi.getBallOperateButton().get()) {
    		shooterWheels.setPower(1.0);
    		shooterKicker.setEnabled(true);
    	} else {
    		shooterWheels.setPower(0);
    		shooterKicker.setEnabled(false);
    	}
    }

    // run continuously waiting for input until interrupted //
    protected boolean isFinished() {
    	return false;
    }

    // ensure everything is reset //
    protected void end() {
    	shooterWheels.setPower(0);
    	shooterKicker.setEnabled(false);
    }

    protected void interrupted() {
    	this.end();
    }
}
