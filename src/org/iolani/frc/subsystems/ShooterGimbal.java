package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
//import org.iolani.frc.commands.debug.*;
import org.iolani.frc.commands.HoldGimbalPosition;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class ShooterGimbal extends Subsystem {

	private TalonSRX _azimuth;
	private TalonSRX _elevation;
	
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
	
	private static final int    ENCODER_TICKS_PER_REV = 4096;
	
	private static final double AZIMUTH_PINION_TEETH     = 12;
	private static final double AZIMUTH_GEAR_TEETH       = 120;
	private static final double AZIMUTH_DEGREES_PER_REV  = (AZIMUTH_PINION_TEETH / AZIMUTH_GEAR_TEETH) * 360;
	private static final double AZIMUTH_DEGREES_PER_TICK = AZIMUTH_DEGREES_PER_REV / ENCODER_TICKS_PER_REV;
	private static final double AZIMUTH_POT_FULL_SCALE   = (POT_TURNS * 360) * AZIMUTH_PINION_TEETH / AZIMUTH_GEAR_TEETH;
	
	private static final double ELEVATION_PINION_TEETH    = 14;
	private static final double ELEVATION_GEAR_TEETH      = 160;
	private static final double ELEVATION_DEGREES_PER_REV = (ELEVATION_PINION_TEETH / ELEVATION_GEAR_TEETH) * 360;
	private static final double ELEVATION_DEGREES_PER_TICK = ELEVATION_DEGREES_PER_REV / ENCODER_TICKS_PER_REV;
	private static final double ELEVATION_POT_FULL_SCALE  = (POT_TURNS * 360) * ELEVATION_PINION_TEETH / ELEVATION_GEAR_TEETH;
	
	
	// calibration constants //
	private static final double AZIMUTH_OFFSET_DEGREES   = -285.0;
	private static final double ELEVATION_OFFSET_DEGREES = -150.7;
	
    public void init() {
    	_azimuth = new TalonSRX(RobotMap.shooterAzimuth);
    	_azimuth.setNeutralMode(NeutralMode.Brake);
    	_azimuth.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    	
    	_azimuth.configForwardSoftLimitThreshold(Math.round((float) (AZIMUTH_DEGREES_MAX / AZIMUTH_DEGREES_PER_TICK)), 0);
    	_azimuth.configForwardSoftLimitEnable(true, 0);
    	_azimuth.configReverseSoftLimitThreshold(Math.round((float) (AZIMUTH_DEGREES_MIN / AZIMUTH_DEGREES_PER_TICK)), 0);
    	_azimuth.configReverseSoftLimitEnable(true, 0);
    	
    	_azimuth.configNominalOutputForward(+0.0f, 0);
    	_azimuth.configNominalOutputReverse(-0.0f, 0);
    	_azimuth.configPeakOutputForward(+12.0f, 0);
    	_azimuth.configPeakOutputReverse(-12.0f, 0);
    	
    	// position profile //
    	_azimuth.selectProfileSlot(1, 0);
    	_azimuth.config_kF(1, 0, 0);
    	_azimuth.config_kP(1, 5, 0);
    	_azimuth.config_kI(1, 0, 0);
    	_azimuth.config_kD(1, 240, 0);
    	
    	// azimuth pot //
    	_azimuthPot = new AnalogPotentiometer(RobotMap.shooterAzimuthADC, AZIMUTH_POT_FULL_SCALE, AZIMUTH_OFFSET_DEGREES);
    	
    	// note: elevation is inverted //
    	_elevation = new TalonSRX(RobotMap.shooterElevation);
    	_elevation.setNeutralMode(NeutralMode.Brake);
    	_elevation.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    	
    	this.setElevationEnvelope(ElevationEnvelope.Full);
    	_elevation.configForwardSoftLimitEnable(true, 0);
    	_elevation.configReverseSoftLimitEnable(true, 0);
    	
    	_elevation.configNominalOutputForward(+0.0f, 0);
    	_elevation.configNominalOutputReverse(-0.0f, 0);
    	_elevation.configPeakOutputForward(+12.0f, 0);
    	_elevation.configPeakOutputReverse(-12.0f, 0);
    	
    	// position profile //
    	_elevation.selectProfileSlot(1, 0);
    	_elevation.config_kF(1, 0, 0);
    	_elevation.config_kP(1, 12, 0);
    	_elevation.config_kI(1, 0, 0);
    	_elevation.config_kD(1, 240, 0);
    	
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
    	_azimuth.setSelectedSensorPosition(0, 0, 0);
    	_elevation.setSelectedSensorPosition(0, 0, 0);
    }
    
    public double getAzimuthDegrees() {
    	return (_azimuth.getSelectedSensorPosition(0) * AZIMUTH_DEGREES_PER_TICK);
    }
 
    public double getAzimuthPotDegrees() {
    	return -_azimuthPot.get();
    }
    
    public double getAzimuthSetpointDegrees() {
    	return _azimuthSetpoint;
    }
    
    public void setAzimuthSetpointDegrees(double degrees) {
    	_azimuth.selectProfileSlot(1, 0);
    	_azimuth.set(ControlMode.Position, degrees / AZIMUTH_DEGREES_PER_TICK);
    	_azimuthSetpoint = degrees;
    }
 
    public double getAzimuthErrorDegrees() {
    	return this.getAzimuthDegrees() - this.getAzimuthSetpointDegrees();
    }
    
    public double getElevationDegrees() {
    	return -(_elevation.getSelectedSensorPosition(0) * ELEVATION_DEGREES_PER_TICK);
    }
    
    public void setElevationPosition(double degrees) {
    	_elevation.setSelectedSensorPosition(Math.round((float) (-degrees / ELEVATION_DEGREES_PER_TICK)), 0, 0);	
    }
    
    public double getElevationPotDegrees() {
    	return -_elevationPot.get();
    }
    
    public double getElevationSetpointDegrees() {
    	return -_elevationSetpoint;
    }
    
    public void setElevationSetpointDegrees(double degrees) {
    	_elevation.selectProfileSlot(1, 0);
    	_elevation.set(ControlMode.Position, -(degrees / ELEVATION_DEGREES_PER_TICK));
    	_elevationSetpoint = -degrees;
    }

    public double getElevationErrorDegrees() {
    	return this.getElevationDegrees() - this.getElevationSetpointDegrees();
    }
    
    public void setElevationEnvelope(ElevationEnvelope env) {
    	switch(env) {
    		case Full:
    	    	_elevation.configForwardSoftLimitThreshold(Math.round((float) (-ELEVATION_DEGREES_MIN / ELEVATION_DEGREES_PER_TICK)), 0);
    	    	_elevation.configReverseSoftLimitThreshold(Math.round((float) (-ELEVATION_DEGREES_MAX / ELEVATION_DEGREES_PER_TICK)), 0);
    	    	break;
    		case Shot:
    			_elevation.configForwardSoftLimitThreshold(Math.round((float) (-ELEVATION_SHOT_DEGREES_MIN / ELEVATION_DEGREES_PER_TICK)), 0);
    	    	_elevation.configReverseSoftLimitThreshold(Math.round((float) (-ELEVATION_SHOT_DEGREES_MAX / ELEVATION_DEGREES_PER_TICK)), 0);
    			break;
    	}
    }
    
    // positive is clockwise //
    public void setAzimuthPower(double power) {
    	_azimuthSetpoint = 0;
    	_azimuth.set(ControlMode.PercentOutput, power);
    }
    
    // positive is up //
    public void setElevationPower(double power) {
    	_elevationSetpoint = 0;
    	_elevation.set(ControlMode.PercentOutput, -power);
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

