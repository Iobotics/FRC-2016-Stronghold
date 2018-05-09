package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetIntakePower;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class Intake extends Subsystem {
	
	private static final int ENCODER_TICKS_PER_REV = 4096;

	public enum RampPosition {
		Retracted,
		Deployed
	}
	
	private TalonSRX _intake;
    private Solenoid _valve;
	
    public void init() {
    	_intake = new TalonSRX(RobotMap.intakeTalon);
    	_intake.setNeutralMode(NeutralMode.Brake);
    	_intake.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
    	
    	// PID control parameters //
    	_intake.selectProfileSlot(1, 0);
    	_intake.config_kF(1, 0, 0);
    	_intake.config_kP(1, 1, 0);
    	_intake.config_kI(1, 0, 0);
    	_intake.config_kD(1, 0, 0);
    	
    	_valve = new Solenoid(RobotMap.intakeValve);
    	this.setRampPosition(RampPosition.Retracted);
    }
    
    // positive is in //
    public void setPower(double power) {
    	_intake.set(ControlMode.PercentOutput, power);
    }

    public void setRampPosition(RampPosition pos) {
    	switch(pos) {
    		case Retracted: _valve.set(false); return;
    		case Deployed:  _valve.set(true);  return;
    	}
    }
    
    public RampPosition getRampPosition() {
    	return _valve.get()? RampPosition.Deployed: RampPosition.Retracted;
    }
    
    public double getIntakePositionDegrees() {
    	return _intake.getSelectedSensorPosition(0) / (ENCODER_TICKS_PER_REV / 360.0);
    }
    
    public void setIntakePositionDegrees(double angle) {
    	_intake.set(ControlMode.Position, ((angle / 360.0) * ENCODER_TICKS_PER_REV));
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetIntakePower(0.0, RampPosition.Retracted));
    }
    
    public void debug() {
    	SmartDashboard.putNumber("intake-position", this.getIntakePositionDegrees());
    }
}

