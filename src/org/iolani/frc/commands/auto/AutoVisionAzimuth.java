/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.commands.SeekGimbalToVision;
import org.iolani.frc.subsystems.ShooterGimbal;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;
import org.iolani.frc.util.Utility;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class AutoVisionAzimuth extends PIDCommand {
   
	private static final double SPEED_DEG_PER_SEC = 3.5;
	private static final double STEP_SIZE_DEGREES = 1.0;
	
	
	private double _elevationCurrent;
	private double _lastTime;
	private int    _lastPOV;
	private final NetworkTable _table;
	
    public AutoVisionAzimuth(double timeout) {
    	super("GimbalVisionPID", SeekGimbalToVision.K_P, SeekGimbalToVision.K_I, SeekGimbalToVision.K_D);
        requires(CommandBase.shooterGimbal);
        _table = NetworkTable.getTable("SmartDashboard");
        //SmartDashboard.putData("GimbalPID", this.getPIDController());
        this.getPIDController().setOutputRange(-SeekGimbalToVision.MAX_OUTPUT, SeekGimbalToVision.MAX_OUTPUT);
        this.setTimeout(timeout);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	CommandBase.shooterGimbal.setElevationSetpointDegrees(CommandBase.shooterGimbal.getElevationDegrees());
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
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return this.isTimedOut();
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
