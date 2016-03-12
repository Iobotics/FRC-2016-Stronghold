/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class OperateTankDrive extends CommandBase {
    
	private final boolean _scaledInput;
	
	public OperateTankDrive() {
		this(true);
	}
	
    public OperateTankDrive(boolean scaledInput) {
        requires(drivetrain);
        _scaledInput = scaledInput;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double left  = oi.getLeftStick().getY();
    	double right = oi.getRightStick().getY();
        
        // signal conditioning //
        /*PowerScaler driveScale = oi.getPowerScaler();
        if(driveScale != null) {
            mag = driveScale.get(mag);
            rot *= Math.abs(rot); // rot^2 preserving sign //
        }
        System.out.println("joysticks: " + mag + ", " + rot);*/
    	
        drivetrain.setTank(left, right, _scaledInput);
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
