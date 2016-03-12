package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake.RampPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SuckBall extends CommandGroup {
	
	public SuckBall() {
    	this.addParallel(new SetIntakeVariablePower(1.0, RampPosition.Deployed));
		this.addParallel(new SetShooterWheelSpeed(-500));
    }
}
