package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */

public class ShooterGimbal extends Subsystem {

	private CANTalon _azimuth;
	private CANTalon _elevation;
    
    public void init() {
    	_azimuth = new CANTalon(RobotMap.shooterAzimuth);
    	_azimuth.enableBrakeMode(true);
    	_elevation = new CANTalon(RobotMap.shooterElevation);
    	_elevation.enableBrakeMode(true);
    }
    
    public void setPower(double azimuthPower, double elevationPower) {
    	this.setAzimuthPower(azimuthPower);
    	this.setElevationPower(elevationPower);
    }
    
    // positive is clockwise //
    public void setAzimuthPower(double power) {
    	_azimuth.set(power);
    }
    
    // positive is up //
    public void setElevationPower(double power) {
    	_elevation.set(power);
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetShooterGimbalPower(0.0, 0.0));
    }
}

