package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutoDelayAndDrive extends CommandGroup {
    
    public AutoDelayAndDrive() {
    	addSequential(new WaitCommand(10));
        addSequential(new AutoDriveStraight(200));
    }
}
