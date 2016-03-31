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

public class ShooterWheels extends Subsystem {

	private CANTalon _left;
	private CANTalon _right;
    
	// note, due to mechanical issues, left is faster //
    public void init() {
    	_left = new CANTalon(RobotMap.shooterWheelLeft);
    	_left.enableBrakeMode(false);
    	_left.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	
    	_left.configNominalOutputVoltage(+0.0f, -0.0f);
    	_left.configPeakOutputVoltage(+12.0f, -12.0f);
    	_left.setProfile(1);
    	_left.setF(0.024);
    	_left.setP(0.05);
    	_left.setI(0);
    	_left.setD(5);
    	
    	_right = new CANTalon(RobotMap.shooterWheelRight);
    	_right.enableBrakeMode(false);
    	_right.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	_right.reverseSensor(true);
    	
    	_right.configNominalOutputVoltage(+0.0f, -0.0f);
    	_right.configPeakOutputVoltage(+12.0f, -12.0f);
    	_right.setProfile(1);
    	_right.setF(0.023);
    	_right.setP(0.05);
    	_right.setI(0);
    	_right.setD(5);
    }
    
    // positive is out //
    public void setPower(double power) {
    	_left.changeControlMode(TalonControlMode.PercentVbus);    	
    	_left.set(-power);
    	_right.changeControlMode(TalonControlMode.PercentVbus);
    	_right.set(-power);
    }

    // positive is out //
    public void setSpeed(double rpm) {
    	_left.changeControlMode(TalonControlMode.Speed);
    	_left.set(-rpm);
    	_right.changeControlMode(TalonControlMode.Speed);
    	_right.set(-rpm);
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetShooterWheelPower(0.0));
    }
    
    public void debug() {
     	SmartDashboard.putNumber("shooter-left-rpm", -(int) _left.getSpeed());
    	SmartDashboard.putNumber("shooter-right-rpm", -(int) _right.getSpeed());
    }
}

