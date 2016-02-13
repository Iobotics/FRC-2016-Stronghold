package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 */

public class ShooterKicker extends Subsystem {

	private Solenoid _kicker;
    
    public void init() {
    	_kicker = new Solenoid(RobotMap.shooterKickValve);
    }
    
    // positive is in //
    public void setEnabled(boolean value) {
    	_kicker.set(value);
    }

    public void initDefaultCommand() {
    	this.setDefaultCommand(new SetShooterKicker(false));
    }
}

