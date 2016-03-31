/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.util.PowerScaler;

/**
 *
 * @author iobotics
 */
public class OperateArcadeDrive extends CommandBase {
    
	private static final PowerScaler _scaler = new PowerScaler(
		new PowerScaler.PowerPoint[] {
            new PowerScaler.PowerPoint(0.0, 0.0),
            new PowerScaler.PowerPoint(0.05, 0.0),
            new PowerScaler.PowerPoint(0.75, 0.5),
            new PowerScaler.PowerPoint(0.90, 1.0)
        });
	
    public OperateArcadeDrive() {
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double mag = -oi.getRightStick().getY();
        double rot = -oi.getLeftStick().getX();
        
        // signal conditioning //
        PowerScaler driveScale = _scaler;
        if(driveScale != null) {
            mag = driveScale.get(mag);
            rot *= Math.abs(rot); // rot^2 preserving sign //
        }
        //System.out.println("joysticks: " + mag + ", " + rot);
        drivetrain.setArcade(mag, rot);
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
