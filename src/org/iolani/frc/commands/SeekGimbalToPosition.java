package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SeekGimbalToPosition extends CommandGroup {
    
	public enum ShooterPosition {
		Home,
		SlotLoad
	}
    
    public SeekGimbalToPosition(ShooterPosition pos) {
    	
    	switch(pos) {
    		case Home:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(0.0));
    			break;
    		case SlotLoad:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(97.0));
    			break;
    		default:
    			throw new IllegalArgumentException("Unknown position: " + pos);
    	}
		
    }
}
