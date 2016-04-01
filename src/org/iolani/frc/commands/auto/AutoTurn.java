package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoTurn extends CommandBase implements PIDOutput {

	private final double  _degrees;
    private PIDController _pid;
    private boolean       _onTarget;
    private double        _onTargetTime;
	
	private static final double kP = 0.050;
	private static final double kI = 0.0;
	private static final double kD = 0.0;
	
    public AutoTurn(double degrees) { //Counterclockwise is positive
    	requires(drivetrain);
    	requires(navsensor);
    	_degrees = degrees;
    	_pid = new PIDController(kP, kI, kD, navsensor, this);
    	_pid.setInputRange(-180.0f,  180.0f);
    	_pid.setContinuous(true);
    	/*_pid.setTolerance(new PIDController.Tolerance() {
			@Override
			public boolean onTarget() {
				return  Math.abs(_pid.getError()) < 1.5;
			}	
		});*/
    	_pid.setAbsoluteTolerance(2.0);
    	_pid.setToleranceBuffer(5);
    	_pid.setOutputRange(-0.5, 0.5);
    	this.setTimeout(10);
    }

    // positive is clockwise //
    public void pidWrite(double power) {
    	// linearize over deadband //
    	if(power > 0 && power < 0.1) power += 0.1;
    	if(power < 0 && power > 0.1) power -= 0.1;
    	
    	SmartDashboard.putNumber("drive-auto-command", power);
    	drivetrain.setTank(power, -power);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	navsensor.zeroGyro();
    	_pid.setSetpoint(_degrees);
    	_pid.enable();
    	_onTargetTime = Double.MAX_VALUE;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("drive-auto-avg-error", _pid.getAvgError());
    	SmartDashboard.putNumber("drive-auto-error", _pid.getError());
    	SmartDashboard.putBoolean("drive-auto-ontarget", _pid.onTarget());
    }

    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	// if we've passed target time, finish //
        if(this.timeSinceInitialized() > _onTargetTime) {
        	return true;
        }
        if(_pid.onTarget() & !_onTarget) {
        	_onTarget = true;
        	_onTargetTime = this.timeSinceInitialized() + 1.0;
        } else if(!_pid.onTarget()){
        	_onTarget = false;
        }
        return false;
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
