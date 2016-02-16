/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.util.PowerScaler;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 *
 * @author iobotics
 */
public class OperateGimbalUnsafe extends CommandBase {
   
	private static final PowerScaler _scaler = new PowerScaler(new PowerScaler.PowerPoint[] {
            new PowerScaler.PowerPoint(0.0, 0.0),
            new PowerScaler.PowerPoint(0.5, 0.1),
            new PowerScaler.PowerPoint(0.95, 0.50),
            new PowerScaler.PowerPoint(1.0, 1.0)
        });
	
    public OperateGimbalUnsafe() {
        requires(shooterGimbal);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double azimuth   = oi.getGunnerStick().getX(Hand.kLeft);
        double elevation = oi.getGunnerStick().getY(Hand.kRight);
        
        // signal conditioning //
        azimuth   = _scaler.get(azimuth);
        elevation = _scaler.get(elevation);
        
        System.out.println("gimbal: " + azimuth + ", " + elevation);
        shooterGimbal.setAzimuthPower(azimuth);
        shooterGimbal.setElevationPower(-elevation);
        
        if(oi.getGunnerStick().getRawButton(1)) {
        	shooterGimbal.resetEncoders();
        }
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
