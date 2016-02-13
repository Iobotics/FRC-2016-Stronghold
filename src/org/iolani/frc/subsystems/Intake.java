package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */

public class Intake extends Subsystem {

	private CANTalon _intake;
    
    public void init() {
    	_intake = new CANTalon(RobotMap.intakeTalon);
    	_intake.enableBrakeMode(true);
    	_intake.setInverted(true);
    }
    
    // positive is in //
    public void setPower(double power) {
    	_intake.set(power);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetIntakePower(0.0));
    }
}

