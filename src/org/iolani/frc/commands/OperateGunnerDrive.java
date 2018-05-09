/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 *
 * @author iobotics
 */
public class OperateGunnerDrive extends CommandBase {
    
    public OperateGunnerDrive() {
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double mag = oi.getGunnerStick().getY(Hand.kLeft) / 2;
        double rot = oi.getGunnerStick().getX(Hand.kLeft) / 1.5;
        
        // signal conditioning //
        //System.out.println("joysticks: " + mag + ", " + rot);
        drivetrain.setArcade(mag, rot, true);
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
