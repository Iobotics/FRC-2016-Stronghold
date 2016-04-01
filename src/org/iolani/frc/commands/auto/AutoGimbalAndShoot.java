package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.SeekGimbalAzimuth;
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

public class AutoGimbalAndShoot extends CommandGroup {

	private static final double VISION_TIME = 5.0;
	
	public AutoGimbalAndShoot(double driveDistance, double initialAzimuth, double elevation) {
		this.addSequential(new AutoDriveStraight(driveDistance));
		this.addParallel(new SeekGimbalElevation(elevation));
		this.addParallel(new SetCameraPosition(CameraPosition.ShotOptimal));
		this.addSequential(new AutoTurn(55.0));
		this.addSequential(new SeekGimbalAzimuth(initialAzimuth));
		this.addSequential(new AutoVisionAzimuth(VISION_TIME));
		this.addSequential(new SetShooterWheelSpeed(5500, true));
		this.addSequential(new WaitCommand(1));
		this.addSequential(new SetShooterKicker(true, true));
		this.addSequential(new WaitCommand(0.25));
		this.addParallel(new SetShooterKicker(false, true));
		this.addParallel(new SetShooterWheelPower(0, true));
		this.addParallel(new SeekGimbalToPosition(GimbalPosition.Home));
	}
}
