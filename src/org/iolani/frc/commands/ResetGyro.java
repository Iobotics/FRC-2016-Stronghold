package org.iolani.frc.commands;

/**
 * Reset the gyro yaw.
 * 
 * @author jmalins
 */
public class ResetGyro extends CommandBase {

    public ResetGyro() {
    }

    // set gunner mode on OI //
    protected void initialize() {
    	navsensor.zeroGyro();
    }

    protected void execute() {
    }

    // terminates immediately //
    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    	this.end();
    }
}
