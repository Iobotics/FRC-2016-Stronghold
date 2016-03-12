package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;
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
    	_valve = new Solenoid(RobotMap.intakeValve);
    	this.setRampPosition(RampPosition.Retracted);
    }
    
    // positive is in //
    public void setPower(double power) {
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
    
    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetIntakePower(0.0, RampPosition.Retracted));
    }
}

