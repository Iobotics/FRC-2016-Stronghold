/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.debug;

import org.iolani.frc.commands.CommandBase;

/**
 *
 * @author iobotics
 */
public class ResetDriveEncoders extends CommandBase {

	
    public ResetDriveEncoders() {
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drivetrain.setLeftEncoderDistance(0);
    	drivetrain.setRightEncoderDistance(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
