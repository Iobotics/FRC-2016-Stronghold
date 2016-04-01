package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoDriveOnly extends CommandGroup {
    
    public AutoDriveOnly() {
        addSequential(new AutoDriveStraight(200));
    }
}
