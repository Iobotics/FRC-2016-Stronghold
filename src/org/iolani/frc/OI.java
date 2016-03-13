
package org.iolani.frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.iolani.frc.util.ConditionalButton;
import org.iolani.frc.util.JoystickAxisThresholdButton;
import org.iolani.frc.util.PowerScaler;
import org.iolani.frc.commands.*;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.debug.OperateGimbalManual;
import org.iolani.frc.commands.debug.OperateGimbalUnsafe;
//import org.iolani.frc.commands.auto.AutoDriveStraight;
//import org.iolani.frc.commands.auto.AutoGrabTrashCan;
//import org.iolani.frc.commands.auto.AutoTurn;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private static final int XSTICK_LTRIGGER_AXIS = 2;
	private static final int XSTICK_RTRIGGER_AXIS = 3;
	
	// persistent virtual button used to store gunner mode state //
	private final InternalButton _gunnerModeState = new InternalButton();
	
	// joysticks //
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
    private final Joystick _xStick = new Joystick(3);
    
    // driver buttons //
    private final Button _intakeButton = new JoystickButton(_rStick, 1);
    private final Button _outakeButton = new JoystickButton(_lStick, 1);
    
    private final Button _ballOperateButton = new JoystickButton(_lStick, 2);
    
    private final Button _homePositionButton  = new JoystickButton(_rStick, 3);
    private final Button _loadPositionButton  = new JoystickButton(_lStick, 3);
    private final Button _clearPositionButton = new JoystickButton(_rStick, 4);
    
    private final Button _gunnerEnableButton = new JoystickButton(_rStick, 2);
    
    // gunner buttons //
    private final Button _gunnerFastButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 3));
    private final Button _gunnerSlowButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 4));
    private final Button _gunnerSpinButton = new ConditionalButton(_gunnerModeState, 
    			new JoystickAxisThresholdButton(_xStick, XSTICK_LTRIGGER_AXIS, 0.25, 1)
    		);
    private final Button _gunnerFireButton = new ConditionalButton(_gunnerModeState, 
			new JoystickAxisThresholdButton(_xStick, XSTICK_RTRIGGER_AXIS, 0.25, 1)
		);
    private final Button _gunnerSuckButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 6));
    
    /*private final JoystickButton _shooterKickButton = new JoystickButton(_lStick, 1);
    private final JoystickButton _shooterOutButton  = new JoystickButton(_lStick, 6);
    private final JoystickButton _shooterInButton   = new JoystickButton(_lStick, 7);
    
    private final JoystickButton _shooterOutButton2 = new JoystickButton(_lStick, 9);
    private final JoystickButton _shooterInButton2  = new JoystickButton(_lStick, 8);
    
    private final JoystickButton _shooterOutButton3 = new JoystickButton(_lStick, 11);
    private final JoystickButton _shooterInButton3  = new JoystickButton(_lStick, 10);
    
    private final JoystickButton _gimbalPositionButton = new JoystickButton(_xStick, 2);
    private final JoystickButton _gimbalPositionButton2 = new JoystickButton(_xStick, 3);
    private final JoystickButton _gimbalPositionButton3 = new JoystickButton(_xStick, 5);
    private final JoystickButton _gimbalPositionButton4 = new JoystickButton(_xStick, 6);
    private final JoystickButton _gimbalPositionButton5 = new JoystickButton(_xStick, 4);
    */
    
    private final PowerScaler _scaler;
    
    public OI() {
        
        // use power scaling from traction mode on 2014 bot //
        _scaler = new PowerScaler(new PowerScaler.PowerPoint[] {
                new PowerScaler.PowerPoint(0.0, 0.0),
                new PowerScaler.PowerPoint(0.05, 0.0),
                new PowerScaler.PowerPoint(0.75, 0.5),
                new PowerScaler.PowerPoint(0.90, 1.0)
            });
        
        _intakeButton.whileHeld(new IntakeBall());
        _outakeButton.whileHeld(new OutakeBall());
        
        _homePositionButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.Home));
        _loadPositionButton.whenPressed(new LoadBall());
        _clearPositionButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.Clearance));
        
        _gunnerEnableButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.GunnerNeutral));
        // disable gunner mode on all driver actions affecting gimbal position //
        //_intakeButton.whenPressed(new SetGunnerControlEnabled(false));
        //_outakeButton.whenPressed(new SetGunnerControlEnabled(false));
        //_homePositionButton.whenPressed(new SetGunnerControlEnabled(false));
        //_loadPositionButton.whenPressed(new SetGunnerControlEnabled(false));
        //_clearPositionButton.whenPressed(new SetGunnerControlEnabled(false));
        
        _gunnerFastButton.whileHeld(new OperateGimbalFast());
        _gunnerSlowButton.whileHeld(new OperateGimbalSlow());
        _gunnerSpinButton.whileHeld(new SetShooterWheelSpeed(5500));
        _gunnerFireButton.whileHeld(new SetShooterKicker(true));
        _gunnerSuckButton.whileHeld(new SetShooterWheelPower(-1.0));
        
        /*_intakeSuckButton.whileHeld(new IntakeBall());
        _intakeBlowButton.whileHeld(new SetIntakeVariablePower(-1.0, RampPosition.Deployed));
        
        _shooterKickButton.whileHeld(new SetShooterKicker(true));
        _shooterOutButton.whileHeld(new SetShooterWheelPower(0.2));
        _shooterInButton.whileHeld(new SetShooterWheelPower(-0.2));
        
        _shooterOutButton2.whileHeld(new SetShooterWheelPower(1.0));
        _shooterInButton2.whileHeld(new SetShooterWheelPower(-1.0));
        
        _shooterOutButton3.whileHeld(new SetShooterWheelSpeed(5500));
        _shooterInButton3.whileHeld(new SetShooterWheelSpeed(-5500));
        
        _gimbalPositionButton.whileHeld(new OperateGimbalUnsafe());
        _gimbalPositionButton2.whileHeld(new OperateGimbalManual());
        _gimbalPositionButton3.whenPressed(new SeekGimbalToPosition(ShooterPosition.Home));
        _gimbalPositionButton4.whenPressed(new SeekGimbalToPosition(ShooterPosition.SlotLoad));
        _gimbalPositionButton5.whileHeld(new OperateGimbalSlow());*/
        
        /*
        _navCalibrateButton.whenPressed(new CalibrateNavigationSensor());
        
        _autoTestButton1.whenPressed(new AutoDriveStraight(36));
        _autoTestButton2.whenPressed(new AutoDriveStraight(120));
        _autoTestButton3.whenPressed(new AutoTurn(90));
        _autoTestButton4.whenPressed(new AutoGrabTrashCan());*/
    }
    
    public Joystick getLeftStick()  {
        return _lStick;
    }
    
    public Joystick getRightStick() {
        return _rStick;
    }
    
    public Button getBallOperateButton() {
    	return _ballOperateButton;
    }
    
    public boolean getGunnerControlEnabled() {
    	return _gunnerModeState.get();
    }
    
    public void setGunnerControlEnabled(boolean enabled) {
    	_gunnerModeState.setPressed(enabled);
    }
    
    public Joystick getGunnerStick() {
    	return _xStick;
    }
    
    public PowerScaler getPowerScaler() {
        return _scaler;
    }
        
    /*public Button getIntakeLeftButton() {
    	return _intakeLeftButton;
    }
    
    public double getLeftTwist() {
    	return _lStick.getTwist();
    }
    
    public Button getIntakeRightButton() {
    	return _intakeRightButton;
    }
    
    public double getRightTwist() {
    	return _rStick.getTwist();
    }*/
}