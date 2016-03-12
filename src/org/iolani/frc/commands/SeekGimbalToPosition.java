package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Seek the gimbal to a pre-defined position, terminating when complete.
 *  
 *  This command seeks azimuth first to prevent any collisions when elevation
 *  is reduced.
 */
public class SeekGimbalToPosition extends CommandGroup {
    
	public enum GimbalPosition {
		Home,
		SlotLoad,
		Clearance,
		GunnerNeutral
	}
    
    public SeekGimbalToPosition(GimbalPosition pos) {
    	
    	switch(pos) {
    		case Home:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(0.0));
    			break;
    		case SlotLoad:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(97.0));
    			break;
    		case Clearance:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(5.0));
    			break;
    		case GunnerNeutral:
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(45.0));
    			break;
    		default:
    			throw new IllegalArgumentException("Unknown position: " + pos);
    	}
		
    }
}
