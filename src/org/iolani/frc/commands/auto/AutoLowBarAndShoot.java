package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.SeekGimbalElevation;
import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.SetCameraPosition;
import org.iolani.frc.commands.SetCameraPosition.CameraPosition;
import org.iolani.frc.commands.SetShooterKicker;
import org.iolani.frc.commands.SetShooterWheelPower;
import org.iolani.frc.commands.SetShooterWheelSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoLowBarAndShoot extends CommandGroup {

	public AutoLowBarAndShoot(boolean vision) {
		this.addSequential(new AutoDriveStraight(228));
		this.addParallel(new SeekGimbalElevation(45.0));
		this.addParallel(new SetCameraPosition(CameraPosition.ShotOptimal));
		this.addSequential(new AutoTurn(55.0));
		if(vision) {
			this.addSequential(new AutoVisionAzimuth(5));
		} else {
			this.addSequential(new WaitCommand(3));
		}
		this.addSequential(new SetShooterWheelSpeed(5500, true));
		this.addSequential(new WaitCommand(1));
		this.addSequential(new SetShooterKicker(true, true));
		this.addSequential(new WaitCommand(0.25));
		this.addParallel(new SetShooterKicker(false, true));
		this.addParallel(new SetShooterWheelPower(0, true));
		this.addParallel(new SeekGimbalToPosition(GimbalPosition.Home));
	}
}
