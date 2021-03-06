package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake.RampPosition;
import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.SetCameraPosition.CameraPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeBall extends CommandGroup {
	
	public IntakeBall() {
		this.addSequential(new SeekGimbalToPosition(GimbalPosition.Home));
		this.addParallel(new SetCameraPosition(CameraPosition.IntakeSight, false));
    	this.addParallel(new SetIntakePower(1.0, RampPosition.Deployed));
		this.addParallel(new SetShooterWheelSpeed(-1000));
    }
}
