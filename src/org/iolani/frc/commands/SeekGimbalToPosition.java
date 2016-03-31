package org.iolani.frc.commands;

import org.iolani.frc.commands.SetCameraPosition.CameraPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.StartCommand;

/**
 *  Seek the gimbal to a pre-defined position, terminating when complete.
 *  
 *  This command seeks azimuth first to prevent any collisions when elevation
 *  is reduced.
 */
public class SeekGimbalToPosition extends CommandGroup {
    
	private static final double HOME_ELEVATION_ANGLE      = 0.0;
	private static final double LOAD_ELEVATION_ANGLE      = 96.0;
	private static final double SHOT_LOW_ANGLE            = 42.0;
	private static final double SHOT_MIDDLE_ANGLE         = 45.0;
	private static final double SHOT_HIGH_ANGLE           = 52.0;
	
	public enum GimbalPosition {
		Home,
		SlotLoad,
		GunnerLow,
		GunnerMiddle,
		GunnerHigh
	}
    
    public SeekGimbalToPosition(GimbalPosition pos) {
    	
    	switch(pos) {
    		case Home:
    			this.addSequential(new SetGunnerControlEnabled(false));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(HOME_ELEVATION_ANGLE));
    			this.addSequential(new SetCameraPosition(CameraPosition.Stowed));
    			break;
    		case SlotLoad:
    			this.addSequential(new SetGunnerControlEnabled(false));
    			this.addSequential(new SetCameraPosition(CameraPosition.Stowed));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(LOAD_ELEVATION_ANGLE));
    			break;
    		case GunnerLow:
    		case GunnerMiddle:
    		case GunnerHigh:
    			double shotAngle = (
    				(pos == GimbalPosition.GunnerLow)? SHOT_LOW_ANGLE:
    				(pos == GimbalPosition.GunnerMiddle)? SHOT_MIDDLE_ANGLE:
    				SHOT_HIGH_ANGLE
    			  ); 
    			this.addSequential(new StartCommand(new SetCameraPosition(CameraPosition.ShotOptimal, false)));
    			this.addSequential(new SeekGimbalAzimuth(0.0));
    			this.addSequential(new SeekGimbalElevation(shotAngle));
    			this.addSequential(new SetGunnerControlEnabled(true));
    			break;
    		default:
    			throw new IllegalArgumentException("Unknown position: " + pos);
    	}
		
    }
}
