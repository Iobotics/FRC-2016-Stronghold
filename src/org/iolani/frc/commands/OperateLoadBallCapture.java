package org.iolani.frc.commands;

import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.subsystems.Intake.RampPosition;

/**
 *  When in load mode, wait for a button press to pulse the shooter wheels to grab the ball.
 */
public class OperateLoadBallCapture extends CommandBase {

	private static final double CAPTURE_TIME = 0.250;
	private static final double SPINUP_TIME  = 0.5;
	private static final double SHOOT_TIME   = 0.250;
	
	private enum State {
		WaitForPress,
		Capture,
		SpinUpShooter,
		FireBall
	}
	private State _state;
	private double _waitTime;
	
    public OperateLoadBallCapture() {
    	requires(shooterWheels);
    	//requires(camera);
    }

    protected void initialize() {
    	_state = State.WaitForPress;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch(_state) {
    		case WaitForPress:
    			shooterWheels.setPower(0);
    			if(oi.getBallOperateButton().get()) {
    				_waitTime = this.timeSinceInitialized() + CAPTURE_TIME;
    				_state = State.Capture;
    			} else if(oi.getAuxOperateButton().get()) {
    				_waitTime = this.timeSinceInitialized() + SPINUP_TIME;
    				_state = State.SpinUpShooter;
    			}
    			break;
    		case Capture:
    			shooterWheels.setPower(-1.0);
    			if(this.timeSinceInitialized() > _waitTime) {
    				_state = State.WaitForPress;
    			}
    			break;
    		case SpinUpShooter:
    			shooterWheels.setPower(1.0);
    			if(this.timeSinceInitialized() > _waitTime) {
    				_waitTime = this.timeSinceInitialized() + SHOOT_TIME;
    				_state = State.FireBall;
    			}
    			break;
    		case FireBall:
    			shooterKicker.setEnabled(true);
    			if(this.timeSinceInitialized() > _waitTime) {
    				shooterWheels.setPower(0);
    				shooterKicker.setEnabled(false);
    				_state = State.WaitForPress;
    			}
    			break;
    	}
    	
    	// emergency ball eject //
    	boolean intakeOp = oi.getIntakeOperateButton().get();
    	intake.setPower(intakeOp? -1: 0);
    	intake.setRampPosition(intakeOp? RampPosition.Deployed: RampPosition.Retracted);
    }

    // run continuously waiting for input until interrupted //
    protected boolean isFinished() {
    	return false;
    }

    // make sure the shooter is off on termination //
    protected void end() {
    	shooterWheels.setPower(0);
    }

    protected void interrupted() {
    	this.end();
    }
}
