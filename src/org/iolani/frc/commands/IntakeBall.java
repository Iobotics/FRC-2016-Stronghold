package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake.RampPosition;
import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeBall extends CommandGroup {
	
	public IntakeBall() {
		this.addSequential(new SeekGimbalToPosition(GimbalPosition.Home));
    	this.addParallel(new SetIntakeVariablePower(1.0, RampPosition.Deployed));
		this.addParallel(new SetShooterWheelSpeed(-1000));
    }
}
