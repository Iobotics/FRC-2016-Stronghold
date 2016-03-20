package org.iolani.frc.commands;


/**
 *
 */
public class SetCameraPosition extends CommandBase {

	private final CameraPosition _position;
	private final boolean _terminate;
	
	public static final double STOWED_POSITION = 0.045;
	public static final double INTAKE_POSITION = 0.80;
	public static final double SHOT_POSITION   = 0.670;
		
	// fixed positions //
	public enum CameraPosition {
	 	Stowed,
	 	IntakeSight,
	 	ShotOptimal
	}	 
	    
	public SetCameraPosition(CameraPosition position) {
		this(position, true);
	}
	
    public SetCameraPosition(CameraPosition position, boolean terminate) {
    	requires(camera);
    	_position = position;
    	_terminate = terminate;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	switch(_position) {
			case Stowed:
				camera.setServoPosition(STOWED_POSITION);
				break;
			case IntakeSight:
				camera.setServoPosition(SHOT_POSITION);
				break;
			case ShotOptimal:
				camera.setServoPosition(SHOT_POSITION);
				break;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _terminate;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
