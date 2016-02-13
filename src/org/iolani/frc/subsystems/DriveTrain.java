/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.OperateArcadeTankDrive;
import org.iolani.frc.util.Utility;

/**
 *
 * @author koluke
 */

public class DriveTrain extends Subsystem {
	// hardware //
    private CANTalon      _left;
    private CANTalon      _leftSlave1;
    private CANTalon      _leftSlave2;
    private CANTalon      _right;
    private CANTalon      _rightSlave1;
    private CANTalon      _rightSlave2;
    private Encoder       _lEncoder;
    private Encoder       _rEncoder;
    //private PIDController _lPID;
    //private PIDController _rPID;
    //private PIDOutput     _lPIDOutput;
    //private PIDOutput     _rPIDOutput;
    
	// physical constants //
	private static final double WHEEL_DIAMETER_INCHES = 8.0;
	private static final double WHEEL_INCHES_PER_REV = Math.PI * WHEEL_DIAMETER_INCHES;
	// encoder constants //
	private static final int    ENCODER_TICKS_PER_REV = 128;
	private static final double ENCODER_INCH_PER_TICK = WHEEL_INCHES_PER_REV / ENCODER_TICKS_PER_REV;
	// PID control constants //
	//private static final double kP = 0.25;
	//private static final double kI = 0.0;
	//private static final double kD = 0.3;
    
    public void init()  {
    	System.out.println("drive init start");
    	
        // configure left //
    	_left = new CANTalon(RobotMap.driveLeftMain);
    	_left.setInverted(true);
        _leftSlave1  = new CANTalon(RobotMap.driveLeftSlave1);
        _leftSlave1.changeControlMode(TalonControlMode.Follower);
        _leftSlave1.set(_left.getDeviceID());
        _leftSlave2  = new CANTalon(RobotMap.driveLeftSlave2);
        _leftSlave2.changeControlMode(TalonControlMode.Follower);
        _leftSlave2.set(_left.getDeviceID());
        
        // configure right //
        _right = new CANTalon(RobotMap.driveRightMain);
        _rightSlave1 = new CANTalon(RobotMap.driveRightSlave1);
        _rightSlave1.changeControlMode(TalonControlMode.Follower);
        _rightSlave1.set(_right.getDeviceID());
        _rightSlave2 = new CANTalon(RobotMap.driveRightSlave2);
        _rightSlave2.changeControlMode(TalonControlMode.Follower);
        _rightSlave2.set(_right.getDeviceID());
        
        _lEncoder = new Encoder(RobotMap.driveLeftEncoderB, RobotMap.driveLeftEncoderA);
        _lEncoder.setDistancePerPulse(ENCODER_INCH_PER_TICK);
        _rEncoder = new Encoder(RobotMap.driveRightEncoderA, RobotMap.driveRightEncoderB);
        _rEncoder.setDistancePerPulse(ENCODER_INCH_PER_TICK);
        
       // _lPID = new PIDController(kP, kI, kD, _lEncoder, _lPIDOutput);
        //_rPID = new PIDController(kP, kI, kD, _rEncoder, _rPIDOutput);
        System.out.println("drive init end");
    }


    public void setTank(double left, double right) {
    	_left.set(left);
    	_right.set(right);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        this.setDefaultCommand(new OperateArcadeTankDrive());
    }
    
    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for forwards/backwards
     * @param rotateValue The value to use for the rotate right/left
     * @param squaredInputs If set, decreases the sensitivity at low speeds
     */
    public void setArcade(double moveValue, double rotateValue, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue   = Utility.limit(moveValue);
        rotateValue = Utility.limit(rotateValue);

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        this.setTank(leftMotorSpeed, rightMotorSpeed);
    }
    
    public Encoder getLeftEncoder() {
    	return _lEncoder;
    }
    
    public Encoder getRightEncoder() {
    	return _rEncoder;
    }
    
    public void debug() {
    	//SmartDashboard.putData("drive-left-encoder", _lEncoder);
    	//SmartDashboard.putData("drive-right-encoder", _rEncoder);
    	SmartDashboard.putNumber("drive-left-ticks", _lEncoder.get());
    	SmartDashboard.putNumber("drive-right-ticks", _rEncoder.get());
    	SmartDashboard.putNumber("drive-left-distance", _lEncoder.getDistance());
    	SmartDashboard.putNumber("drive-right-distance", _rEncoder.getDistance());
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param moveValue The value to use for fowards/backwards
     * @param rotateValue The value to use for the rotate right/left
     */
    public void setArcade(double moveValue, double rotateValue) {
        this.setArcade(moveValue, rotateValue, false);
    }
    
}
