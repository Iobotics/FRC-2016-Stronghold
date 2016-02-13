package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */

public class ShooterWheels extends Subsystem {

	private CANTalon _left;
	private CANTalon _right;
    
    public void init() {
    	_left = new CANTalon(RobotMap.shooterWheelLeft);
    	_left.enableBrakeMode(false);
    	_left.reverseOutput(true);
    	_right = new CANTalon(RobotMap.shooterWheelRight);
    	_right.enableBrakeMode(false);
    }
    
    // positive is in //
    public void setPower(double power) {
    	_left.set(power);
    	_right.set(power);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetShooterWheelPower(0.0));
    }
}

