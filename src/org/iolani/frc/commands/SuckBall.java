package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SuckBall extends CommandGroup {
	
	public SuckBall() {
    	this.addParallel(new SetIntakeVariablePower(1.0));
		this.addParallel(new SetShooterWheelSpeed(-500));
    }
}
