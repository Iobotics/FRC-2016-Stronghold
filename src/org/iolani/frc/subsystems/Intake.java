package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;

import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 */

public class Intake extends Subsystem {

	public enum RampPosition {
		Retracted,
		Deployed
	}
	
	private CANTalon _intake;
    private Solenoid _valve;
	
    public void init() {
    	_intake = new CANTalon(RobotMap.intakeTalon);
    	_intake.enableBrakeMode(true);
    	_intake.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
    	
    	// PID control parameters //
    	_intake.setProfile(1);
    	_intake.setF(0);
    	_intake.setP(1);
    	_intake.setI(0);
    	_intake.setD(0);
    	
    	_valve = new Solenoid(RobotMap.intakeValve);
    	this.setRampPosition(RampPosition.Retracted);
    }
    
    // positive is in //
    public void setPower(double power) {
    	_intake.changeControlMode(TalonControlMode.PercentVbus);
    	_intake.set(power);
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
    	return _intake.getPosition();
    }
    
    public void setIntakePositionDegrees(double angle) {
    	_intake.changeControlMode(TalonControlMode.Position);
    	_intake.setSetpoint(angle);
    }
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetIntakePower(0.0, RampPosition.Retracted));
    }
    
    public void debug() {
    	SmartDashboard.putNumber("intake-position", this.getIntakePositionDegrees());
    }
}

