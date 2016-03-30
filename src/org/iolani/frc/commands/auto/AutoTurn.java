package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 */
public class AutoTurn extends CommandBase implements PIDOutput {

	private final double  _degrees;
    private PIDController _pid;
	
	private static final double kP = 0.1;
	private static final double kI = 0.0;
	private static final double kD = 0.5;
	
    public AutoTurn(double degrees) { //Counterclockwise is positive
    	requires(drivetrain);
    	requires(navsensor);
    	_degrees = -degrees;
    }

    public void pidWrite(double power) {
    	drivetrain.setTank(-power, power);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	if(_pid == null) {
    		_pid = new PIDController(kP, kI, kD, navsensor, this);
    		_pid.setAbsoluteTolerance(3.0); // 3.0 degree tolerance //
        	_pid.setOutputRange(-0.35, 0.35);
    	}
    	navsensor.zeroGyro();
    	_pid.setSetpoint(_degrees);
    	_pid.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _pid.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	_pid.disable();
    	drivetrain.setTank(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
