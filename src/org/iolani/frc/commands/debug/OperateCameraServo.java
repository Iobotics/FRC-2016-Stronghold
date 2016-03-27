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
public class OperateCameraServo extends CommandBase {
	
    public OperateCameraServo() {
        requires(camera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        camera.setServoPosition((oi.getLeftStick().getTwist() + 1) / 2);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
