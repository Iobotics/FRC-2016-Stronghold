package org.iolani.frc.commands;

import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.SetCameraPosition.CameraPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LoadBall extends CommandGroup {
	
	public LoadBall() {
		this.addParallel(new SetCameraPosition(CameraPosition.Stowed));
		this.addParallel(new OperateLoadBallCapture());
		this.addSequential(new SeekGimbalToPosition(GimbalPosition.SlotLoad));
    }
}
