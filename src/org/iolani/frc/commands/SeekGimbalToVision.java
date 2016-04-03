/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.subsystems.ShooterGimbal;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;
//import org.iolani.frc.util.Utility;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class SeekGimbalToVision extends PIDCommand {
   
	public static final double SPEED_DEG_PER_SEC = 3.5;
	public static final double STEP_SIZE_DEGREES = 1.0;
	
	public static final double K_P = 2;
	public static final double K_I = 0;
	public static final double K_D = 0;
	
	public static final double MAX_OUTPUT = 3;
	
	private double _elevationCurrent;
	private double _lastTime;
	private int    _lastPOV;
	private final NetworkTable _table;
	
    public SeekGimbalToVision() {
    	super("GimbalVisionPID", K_P, K_I, K_D);
        requires(CommandBase.shooterGimbal);
        _table = NetworkTable.getTable("SmartDashboard");
        //SmartDashboard.putData("GimbalPID", this.getPIDController());
        this.getPIDController().setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	CommandBase.shooterGimbal.setElevationEnvelope(ElevationEnvelope.Shot);
    	_elevationCurrent = CommandBase.shooterGimbal.getElevationDegrees();
    	this.setSetpoint(0.0); // home to zero //
    }

    // use the vision error as the PID input //
	@Override
	protected double returnPIDInput() {
		return _table.getNumber("vision-x-error", 0);
	}

	// control the azimuth power with PID output //
	@Override
	protected void usePIDOutput(double output) {
		double azimuth = CommandBase.shooterGimbal.getAzimuthDegrees();
		SmartDashboard.putNumber("vision-command", output);
		CommandBase.shooterGimbal.setAzimuthSetpointDegrees(azimuth + output);
		
		/*double dist = _table.getNumber("vision-distance", 45);
		double elevation = -0.139*dist + 64.7;
		SmartDashboard.putNumber("vision-elevation", elevation);
		CommandBase.shooterGimbal.setElevationSetpointDegrees(elevation);*/
	}

	@Override
	protected void execute() {
		double elevation = CommandBase.oi.getGunnerStick().getY();
        
        double deltaTime = (this.timeSinceInitialized() - _lastTime);
        double deltaStep = SPEED_DEG_PER_SEC * deltaTime;
        
        int pov = CommandBase.oi.getGunnerStick().getPOV(); 
        if(pov != _lastPOV) {
        	switch(pov) {
        		case 0:
        			_elevationCurrent -= STEP_SIZE_DEGREES;
        			break;
        		case 180:
        			_elevationCurrent += STEP_SIZE_DEGREES;
        			break;
        	}
        	_lastPOV = pov;
        } else {
        	_elevationCurrent += elevation * deltaStep;
        }
        _lastTime = this.timeSinceInitialized();
        
        // enforce limits to prevent run-up //
        if(_elevationCurrent < ShooterGimbal.ELEVATION_SHOT_DEGREES_MIN) {
        	_elevationCurrent = ShooterGimbal.ELEVATION_SHOT_DEGREES_MIN;
        } else if(_elevationCurrent > ShooterGimbal.ELEVATION_SHOT_DEGREES_MAX) {
        	_elevationCurrent = ShooterGimbal.ELEVATION_SHOT_DEGREES_MAX;
        }
        
        CommandBase.shooterGimbal.setElevationSetpointDegrees(_elevationCurrent);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

    // Called once after isFinished returns true
    protected void end() {
    	//CommandBase.shooterGimbal.setAzimuthPower(0);
    	//CommandBase.shooterGimbal.setElevationPower(0);
    	SmartDashboard.putNumber("vision-command", 0);
    	CommandBase.shooterGimbal.setElevationEnvelope(ElevationEnvelope.Full);	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
