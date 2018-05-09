/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.debug;

import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

/**
 *
 * @author iobotics
 */
public class TuneGimbalPID extends CommandBase {
   
	
    public TuneGimbalPID() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shooterGimbal.setElevationEnvelope(ElevationEnvelope.Full);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	XboxController gstick = oi.getGunnerStick();
        if(gstick.getRawButton(1)) {
        	// down //
        	shooterGimbal.setDebugOutput(true);
        	shooterGimbal.setElevationSetpointDegrees(20);
        } else if(gstick.getRawButton(4)) {
        	// up //
        	shooterGimbal.setDebugOutput(true);
        	shooterGimbal.setElevationSetpointDegrees(65);        	
        } else if(gstick.getRawButton(2)) {
        	// right //
        	shooterGimbal.setDebugOutput(true);
        	shooterGimbal.setAzimuthSetpointDegrees(35);
        } else if(gstick.getRawButton(3)) {
        	// left //
        	shooterGimbal.setDebugOutput(true);
        	shooterGimbal.setAzimuthSetpointDegrees(-35);
        } else if(gstick.getRawButton(8)) {
        	// center //
        	shooterGimbal.setDebugOutput(true);
        	shooterGimbal.setAzimuthSetpointDegrees(0);
        	shooterGimbal.setElevationSetpointDegrees(45);
        } else {
        	shooterGimbal.setDebugOutput(false);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooterGimbal.setAzimuthPower(0);
    	shooterGimbal.setElevationPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
