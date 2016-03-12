package org.iolani.frc.commands;

import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Seek the gimbal to a position so the gunner can take over. Once gunner position is reached,
 *  enable gunner control mode.
 *  
 *  @author jmalins
 */
public class SeekToGunnerPosition extends CommandGroup {
	
	public SeekToGunnerPosition() {
		this.addSequential(new SeekGimbalToPosition(GimbalPosition.GunnerNeutral));
		this.addParallel(new SetGunnerControlEnabled(true));
    }
}
