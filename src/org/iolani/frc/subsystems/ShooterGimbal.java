package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;
//import org.iolani.frc.commands.debug.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 */

public class ShooterGimbal extends Subsystem {

	private CANTalon _azimuth;
	private CANTalon _elevation;
	
	public AnalogPotentiometer _azimuthPot;
	public AnalogPotentiometer _elevationPot;
	
	public DigitalOutput _debugDO;
	
	private double _azimuthSetpoint;
	private double _elevationSetpoint;
	
	public enum ElevationEnvelope {
		Full,
		Shot
	}
	
	// limits //
	public static final double AZIMUTH_DEGREES_MIN        = -36;
	public static final double AZIMUTH_DEGREES_MAX        = 36;
	public static final double ELEVATION_DEGREES_MIN      = 0;
	public static final double ELEVATION_DEGREES_MAX      = 97;
	public static final double ELEVATION_SHOT_DEGREES_MIN = 30;
	public static final double ELEVATION_SHOT_DEGREES_MAX = 60;
	
	// physical constants //
	private static final double POT_TURNS = 10;
	
	private static final double AZIMUTH_PINION_TEETH    = 12;
	private static final double AZIMUTH_GEAR_TEETH      = 120;
	private static final double AZIMUTH_DEGREES_PER_REV = (AZIMUTH_PINION_TEETH / AZIMUTH_GEAR_TEETH) * 360;
	private static final double AZIMUTH_POT_FULL_SCALE  = (POT_TURNS * 360) * AZIMUTH_PINION_TEETH / AZIMUTH_GEAR_TEETH;
	
	private static final double ELEVATION_PINION_TEETH    = 14;
	private static final double ELEVATION_GEAR_TEETH      = 160;
	private static final double ELEVATION_DEGREES_PER_REV = (ELEVATION_PINION_TEETH / ELEVATION_GEAR_TEETH) * 360;
	private static final double ELEVATION_POT_FULL_SCALE  = (POT_TURNS * 360) * ELEVATION_PINION_TEETH / ELEVATION_GEAR_TEETH;
	
	
	// calibration constants //
	private static final double AZIMUTH_OFFSET_DEGREES   = -285.0;
	private static final double ELEVATION_OFFSET_DEGREES = -150.7;
	
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
    	
    	// position profile //
    	_azimuth.setProfile(1);
    	_azimuth.setF(0);
    	_azimuth.setP(5);
    	_azimuth.setI(0);
    	_azimuth.setD(240);
    	
    	// azimuth pot //
    	_azimuthPot = new AnalogPotentiometer(RobotMap.shooterAzimuthADC, AZIMUTH_POT_FULL_SCALE, AZIMUTH_OFFSET_DEGREES);
    	
    	// note: elevation is inverted //
    	_elevation = new CANTalon(RobotMap.shooterElevation);
    	_elevation.enableBrakeMode(true);
    	_elevation.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	
    	this.setElevationEnvelope(ElevationEnvelope.Full);
    	_elevation.enableForwardSoftLimit(true);
    	_elevation.enableReverseSoftLimit(true);
    	
    	_elevation.configNominalOutputVoltage(+0.0f, -0.0f);
    	_elevation.configPeakOutputVoltage(+12.0f, -12.0f);
    	
    	// position profile //
    	_elevation.setProfile(1);
    	_elevation.setF(0);
    	_elevation.setP(12);
    	_elevation.setI(0);
    	_elevation.setD(240);
    	
    	// elevation pot //
    	_elevationPot = new AnalogPotentiometer(RobotMap.shooterElevationADC, ELEVATION_POT_FULL_SCALE, ELEVATION_OFFSET_DEGREES);
    	this.setElevationPosition(this.getElevationPotDegrees());
    	
    	// debug DIO: used for scope trigger during step response tuning //
    	_debugDO = new DigitalOutput(RobotMap.shooterDebugDIO);
    	_debugDO.set(false);
    	
    	// clear setpoints //
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
 
    public double getAzimuthPotDegrees() {
    	return -_azimuthPot.get();
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
    
    public double getElevationDegrees() {
    	return -_elevation.getPosition() * ELEVATION_DEGREES_PER_REV;
    }
    
    public void setElevationPosition(double degrees) {
    	_elevation.setPosition(-degrees / ELEVATION_DEGREES_PER_REV);	
    }
    
    public double getElevationPotDegrees() {
    	return -_elevationPot.get();
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
    
    public void setElevationEnvelope(ElevationEnvelope env) {
    	switch(env) {
    		case Full:
    	    	_elevation.setForwardSoftLimit(-ELEVATION_DEGREES_MIN / ELEVATION_DEGREES_PER_REV);
    	    	_elevation.setReverseSoftLimit(-ELEVATION_DEGREES_MAX / ELEVATION_DEGREES_PER_REV);
    	    	break;
    		case Shot:
    			_elevation.setForwardSoftLimit(-ELEVATION_SHOT_DEGREES_MIN / ELEVATION_DEGREES_PER_REV);
    	    	_elevation.setReverseSoftLimit(-ELEVATION_SHOT_DEGREES_MAX / ELEVATION_DEGREES_PER_REV);
    			break;
    	}
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
    
    public void setDebugOutput(boolean value) {
    	_debugDO.set(value);
    }
    
    public void initDefaultCommand() {
    	//this.setDefaultCommand(new TuneGimbalPID());
    	//this.setDefaultCommand(new OperateGimbalUnsafe());
    	this.setDefaultCommand(new HoldGimbalPosition());
    }
    
    public void debug() {
     	SmartDashboard.putNumber("gimbal-azimuth-position", this.getAzimuthDegrees());
     	SmartDashboard.putNumber("gimbal-azimuth-pot", this.getAzimuthPotDegrees());
    	SmartDashboard.putNumber("gimbal-elevation-position", this.getElevationDegrees());
    	SmartDashboard.putNumber("gimbal-elevation-pot", this.getElevationPotDegrees());
    }
}

