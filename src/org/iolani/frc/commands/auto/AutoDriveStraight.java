package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import org.iolani.frc.commands.CommandBase;

/**
 *
 */
public class AutoDriveStraight extends CommandBase implements PIDOutput, PIDSource {
	
	private final double  _distance;
	private PIDController _pid;
	
	private static final double kP = 0.20;
	private static final double kI = 0.0;
	private static final double kD = 0.4;
	
	private static final double kTurn = 0.1;
	
	public AutoDriveStraight(double inches) {
    	this(inches, -1);
    }
	
    public AutoDriveStraight(double inches, double timeout) {
    	requires(drivetrain);
    	requires(navsensor);
    	//_distance = -inches * (12.0 / 13.0);
    	_distance = inches;
    	if(timeout > 0) {
    		this.setTimeout(timeout);
    	}
    }

    public void pidWrite(double leftPower) {
    	double rightPower = leftPower + kTurn * navsensor.getGyro();
    	// FIXME. setTank is backwards //
    	drivetrain.setTank(-leftPower, -rightPower);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	if(_pid == null) {
    		_pid = new PIDController(kP, kI, kD, this, this);
    		_pid.setAbsoluteTolerance(1.0); // 2 inch tolerance //
        	_pid.setOutputRange(-0.35, 0.35);
    	}
    	drivetrain.setLeftEncoderDistance(0.0);
    	navsensor.zeroGyro();
    	_pid.setSetpoint(_distance);
    	_pid.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _pid.onTarget() || this.isTimedOut();
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

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return drivetrain.getLeftEncoderDistance();
	}
}
