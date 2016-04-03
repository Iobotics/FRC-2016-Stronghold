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
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoLowBarAndShoot3 extends CommandGroup {

	public AutoLowBarAndShoot3(boolean vision) {
		this.addSequential(new AutoDriveStraight(100, 0.25));
		this.addSequential(new PrintCommand("Phase 1"));
		this.addSequential(new AutoDriveStraight(140, 0.45));
		this.addSequential(new PrintCommand("Phase 2"));
		this.addParallel(new SeekGimbalElevation(45.0));
		this.addParallel(new SetCameraPosition(CameraPosition.ShotOptimal));
		this.addSequential(new AutoTurn(55.0));
		if(vision) {
			this.addSequential(new AutoVisionAzimuth(4));
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
