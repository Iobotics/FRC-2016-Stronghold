package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetShooterWheelPower;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class ShooterWheels extends Subsystem {

	private TalonSRX _left;
	private TalonSRX _right;
    
	// note, due to mechanical issues, left is faster //
    public void init() {
    	_left = new TalonSRX(RobotMap.shooterWheelLeft);
    	_left.setNeutralMode(NeutralMode.Coast);
    	_left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    	
    	_left.configNominalOutputForward(+0.0f, 0);
    	_left.configNominalOutputForward(-0.0f, 0);
    	_left.configPeakOutputForward(+12.0f, 0);
    	_left.configPeakOutputReverse(-12.0f, 0);
    	_left.selectProfileSlot(1, 0);
    	_left.config_kF(1, 0.023, 0);
    	_left.config_kP(1, 0.05, 0);
    	_left.config_kI(1, 0, 0);
    	_left.config_kD(1, 5, 0);
    	
    	_right = new TalonSRX(RobotMap.shooterWheelRight);
    	_right.setNeutralMode(NeutralMode.Coast);
    	_right.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    	_right.setSensorPhase(true);
    	
    	_right.configNominalOutputForward(+0.0f, 0);
    	_right.configNominalOutputForward(-0.0f, 0);
    	_right.configPeakOutputForward(+12.0f, 0);
    	_right.configPeakOutputReverse(-12.0f, 0);
    	_right.selectProfileSlot(1, 0);
    	_right.config_kF(1, 0.024, 0);
    	_right.config_kP(1, 0.05, 0);
    	_right.config_kI(1, 0, 0);
    	_right.config_kD(1, 5, 0);
    }
    
    // positive is out //
    public void setPower(double power) {
    	_left.set(ControlMode.PercentOutput, -power);
    	_right.set(ControlMode.PercentOutput, -power);
    }

    // positive is out //
    public void setSpeed(double rpm) {
    	_left.set(ControlMode.Velocity, -rpm);
    	_right.set(ControlMode.Velocity, -rpm);
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetShooterWheelPower(0.0));
    }
    
    public void debug() {
     	SmartDashboard.putNumber("shooter-left-rpm", -(int) _left.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("shooter-right-rpm", -(int) _right.getSelectedSensorVelocity(0));
    }
}

