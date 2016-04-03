package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.ResetGyro;
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

public class AutoLowBarAndShoot2 extends CommandGroup {

	public AutoLowBarAndShoot2() {
		this.addSequential(new AutoDriveStraight(120));
		this.addSequential(new ResetGyro()); // reset gyro //
		// make a slight leftward arch to clear wall //
		this.addSequential(new AutoDriveDeadReckon(120, AutoDriveDeadReckon.DEFAULT_POWER, AutoDriveDeadReckon.DEFAULT_POWER * 0.9));
		this.addParallel(new SeekGimbalElevation(47.0));
		this.addParallel(new SetCameraPosition(CameraPosition.ShotOptimal));
		this.addSequential(new AutoTurn(55.0, false)); // turn remainder of 55 deg //
		this.addSequential(new AutoVisionAzimuth(5));
		this.addSequential(new SetShooterWheelSpeed(5500, true));
		this.addSequential(new WaitCommand(1));
		this.addSequential(new SetShooterKicker(true, true));
		this.addSequential(new WaitCommand(0.25));
		this.addParallel(new SetShooterKicker(false, true));
		this.addParallel(new SetShooterWheelPower(0, true));
		this.addParallel(new SeekGimbalToPosition(GimbalPosition.Home));
	}
}
