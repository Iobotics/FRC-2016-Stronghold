package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 *
 */

public class ShooterGimbal extends Subsystem {

	private CANTalon _azimuth;
	private CANTalon _elevation;
	
	private double _azimuthSetpoint;
	private double _elevationSetpoint;
    
	// physical constants //
	private static final double AZIMUTH_PINION_TEETH    = 12;
	private static final double AZIMUTH_GEAR_TEETH      = 120;
	private static final double AZIMUTH_DEGREES_PER_REV = (AZIMUTH_PINION_TEETH / AZIMUTH_GEAR_TEETH) * 360;
	private static final double AZIMUTH_DEGREES_MIN     = -40;
	private static final double AZIMUTH_DEGREES_MAX     = 40;
	
	private static final double ELEVATION_PINION_TEETH    = 14;
	private static final double ELEVATION_GEAR_TEETH      = 160;
	private static final double ELEVATION_DEGREES_PER_REV = (ELEVATION_PINION_TEETH / ELEVATION_GEAR_TEETH) * 360;
	private static final double ELEVATION_DEGREES_MIN     = 0;
	private static final double ELEVATION_DEGREES_MAX     = 97;
	
    public void init() {
    	_azimuth = new CANTalon(RobotMap.shooterAzimuth);
    	_azimuth.enableBrakeMode(true);
    	_azimuth.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	
    	_azimuth.setForwardSoftLimit(AZIMUTH_DEGREES_MAX / AZIMUTH_DEGREES_PER_REV);
    	_azimuth.enableForwardSoftLimit(true);
    	_azimuth.setReverseSoftLimit(AZIMUTH_DEGREES_MIN / AZIMUTH_DEGREES_PER_REV);
    	_azimuth.enableReverseSoftLimit(true);
    	
    	_azimuth.configNominalOutputVoltage(+0.0f, -0.0f);
    	_azimuth.configPeakOutputVoltage(+12.0f, -12.0f);
    	
    	// velocity profile //
    	_azimuth.setProfile(0);
    	_azimuth.setF(0.1);
    	_azimuth.setP(0);
    	_azimuth.setI(0);
    	_azimuth.setD(0);
    	
    	// position profile //
    	_azimuth.setProfile(1);
    	_azimuth.setF(0);
    	_azimuth.setP(6);
    	_azimuth.setI(0.015);
    	_azimuth.setD(400);
    	
    	// note: elevation is inverted //
    	_elevation = new CANTalon(RobotMap.shooterElevation);
    	_elevation.enableBrakeMode(true);
    	_elevation.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	
    	_elevation.setForwardSoftLimit(-ELEVATION_DEGREES_MIN / ELEVATION_DEGREES_PER_REV);
    	_elevation.enableForwardSoftLimit(true);
    	_elevation.setReverseSoftLimit(-ELEVATION_DEGREES_MAX / ELEVATION_DEGREES_PER_REV);
    	_elevation.enableReverseSoftLimit(true);
    	
    	_elevation.configNominalOutputVoltage(+0.0f, -0.0f);
    	_elevation.configPeakOutputVoltage(+12.0f, -12.0f);
    	
    	// velocity profile //
    	_elevation.setProfile(0);
    	_elevation.setF(0.1);
    	_elevation.setP(0);
    	_elevation.setI(0);
    	_elevation.setD(0);
    	
    	// position profile //
    	_elevation.setProfile(1);
    	_elevation.setF(0);
    	_elevation.setP(8);
    	_elevation.setI(0.01);
    	_elevation.setD(400);
    	
    	_azimuthSetpoint   = 0;
    	_elevationSetpoint = 0;
    }
    
    public void resetEncoders() {
    	_azimuth.setPosition(0);
    	_elevation.setPosition(0);
    }
    
    public double getAzimuthDegrees() {
    	return _azimuth.getPosition() * AZIMUTH_DEGREES_PER_REV;
    }
 
    public double getAzimuthSetpointDegrees() {
    	return _azimuthSetpoint;
    }
    
    public void setAzimuthSetpointDegrees(double degrees) {
    	_azimuth.setProfile(1);
    	_azimuth.changeControlMode(TalonControlMode.Position);
    	_azimuth.set(degrees / AZIMUTH_DEGREES_PER_REV);
    	_azimuthSetpoint = degrees;
    }
 
    public double getAzimuthErrorDegrees() {
    	return this.getAzimuthDegrees() - this.getAzimuthSetpointDegrees();
    }
    
    public void setAzimuthSpeed(double degreesPerSecond) {
    	_azimuth.setProfile(0);
    	_azimuth.changeControlMode(TalonControlMode.Speed);
    	_azimuth.set(degreesPerSecond / AZIMUTH_DEGREES_PER_REV * 60);
    	_azimuthSetpoint = 0;
    }
    
    public double getElevationDegrees() {
    	return -_elevation.getPosition() * ELEVATION_DEGREES_PER_REV;
    }
    
    public double getElevationSetpointDegrees() {
    	return -_elevationSetpoint;
    }
    
    public void setElevationSetpointDegrees(double degrees) {
    	_elevation.setProfile(1);
    	_elevation.changeControlMode(TalonControlMode.Position);
    	_elevation.set(-degrees / ELEVATION_DEGREES_PER_REV);
    	_elevationSetpoint = -degrees;
    }

    public double getElevationErrorDegrees() {
    	return this.getElevationDegrees() - this.getElevationSetpointDegrees();
    }
    
    public void setElevationSpeed(double degreesPerSecond) {
    	_elevation.setProfile(1);
    	_elevation.changeControlMode(TalonControlMode.Speed);
    	_elevation.set(degreesPerSecond / ELEVATION_DEGREES_PER_REV * 60);
    	_elevationSetpoint = 0;
    }
    
    // positive is clockwise //
    public void setAzimuthPower(double power) {
    	_azimuthSetpoint = 0;
    	_azimuth.changeControlMode(TalonControlMode.PercentVbus);
    	_azimuth.set(power);
    }
    
    // positive is up //
    public void setElevationPower(double power) {
    	_elevationSetpoint = 0;
    	_elevation.changeControlMode(TalonControlMode.PercentVbus);  
    	_elevation.set(-power);
    }
    
    public void initDefaultCommand() {
    	//this.setDefaultCommand(new SetShooterGimbalPower(0.0, 0.0));
    	//this.setDefaultCommand(new OperateGimbalUnsafe());
    	this.setDefaultCommand(new HoldGimbalPosition());
    }
    
    public void debug() {
     	SmartDashboard.putNumber("gimbal-azimuth-position", this.getAzimuthDegrees());
    	SmartDashboard.putNumber("gimbal-elevation-position", this.getElevationDegrees());
    }
}

