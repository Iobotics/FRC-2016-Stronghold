package org.iolani.frc.commands;

import org.iolani.frc.commands.SetCameraPosition.CameraPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Seek the gimbal to a pre-defined position, terminating when complete.
 *  
 *  This command seeks azimuth first to prevent any collisions when elevation
 *  is reduced.
 */
public class SeekGimbalToPosition extends CommandGroup {
    
	private static final double HOME_ELEVATION_ANGLE      = 0.0;
	private static final double LOAD_ELEVATION_ANGLE      = 96.0;
	private static final double CLEARANCE_ELEVATION_ANGLE = 5.0;
	private static final double SHOT_ELEVATION_ANGLE      = 45.0;
	
	public enum GimbalPosition {
		Home,
		SlotLoad,
		Clearance,
		GunnerNeutral
	}
    
    public SeekGimbalToPosition(GimbalPosition pos) {
    	
    	switch(pos) {
    		case Home:
    			this.addParallel(new SetGunnerControlEnabled(false));
    			this.addParallel(new SetCameraPosition(CameraPosition.Stowed));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(HOME_ELEVATION_ANGLE));
    			this.addSequential(new SetHomeStateEnabled(true));
    			break;
    		case SlotLoad:
    			this.addParallel(new SetHomeStateEnabled(false));
    			this.addParallel(new SetGunnerControlEnabled(false));
    			this.addParallel(new SetCameraPosition(CameraPosition.Stowed));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(LOAD_ELEVATION_ANGLE));
    			break;
    		case Clearance:
    			this.addParallel(new SetHomeStateEnabled(false));
    			this.addSequential(new SetGunnerControlEnabled(false));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(CLEARANCE_ELEVATION_ANGLE));
    			break;
    		case GunnerNeutral:
    			this.addParallel(new SetHomeStateEnabled(false));
    			this.addSequential(new SetCameraPosition(CameraPosition.ShotOptimal));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(SHOT_ELEVATION_ANGLE));
    			this.addSequential(new SetGunnerControlEnabled(true));
    			break;
    		default:
    			throw new IllegalArgumentException("Unknown position: " + pos);
    	}
		
    }
}
