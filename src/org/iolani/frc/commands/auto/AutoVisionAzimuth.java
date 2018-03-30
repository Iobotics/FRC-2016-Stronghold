/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.commands.SeekGimbalToVision;
import org.iolani.frc.subsystems.ShooterGimbal.ElevationEnvelope;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author iobotics
 */
public class AutoVisionAzimuth extends PIDCommand {
	
	private final NetworkTable _table;
	
    public AutoVisionAzimuth(double timeout) {
    	super("GimbalVisionPID", SeekGimbalToVision.K_P, SeekGimbalToVision.K_I, SeekGimbalToVision.K_D);
        requires(CommandBase.shooterGimbal);
        _table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
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
		return _table.getEntry("vision-x-error").getDouble(0);
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
