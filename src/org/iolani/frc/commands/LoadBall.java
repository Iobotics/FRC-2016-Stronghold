package org.iolani.frc.commands;

import org.iolani.frc.subsystems.Intake.RampPosition;
import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LoadBall extends CommandGroup {
	
	public LoadBall() {
		this.addSequential(new SeekGimbalToPosition(GimbalPosition.SlotLoad));
		this.addParallel(new OperateLoadBallCapture());
    }
}
