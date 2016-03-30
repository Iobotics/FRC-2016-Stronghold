
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
import org.iolani.frc.commands.SetCameraPosition.CameraPosition;
import org.iolani.frc.commands.auto.AutoDriveStraight;
import org.iolani.frc.commands.debug.RunCameraCommand;
import org.iolani.frc.subsystems.Camera.CameraCommand;
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
	
	// persistent virtual button used to store home mode state //
	private final InternalButton _homeModeState = new InternalButton();
	
	// persistent virtual button used to store gunner mode state //
	private final InternalButton _gunnerModeState = new InternalButton();
	
	// joysticks //
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
    private final Joystick _xStick = new Joystick(3);
    
    // driver buttons //
    private final Button _intakeButton = new JoystickButton(_rStick, 1);
    private final Button _outakeButton = new JoystickButton(_lStick, 1);
    
    private final Button _ballOperateButton   = new JoystickButton(_lStick, 2);
    private final Button _intakeOperateButton = new JoystickButton(_lStick, 5);
    private final Button _auxOperateButton    = new JoystickButton(_lStick, 4);
    
    private final Button _homePositionButton  = new JoystickButton(_rStick, 3);
    private final Button _loadPositionButton  = new JoystickButton(_lStick, 3);
    
    private final Button _gunnerLowButton  = new JoystickButton(_rStick, 4);
    private final Button _gunnerMidButton  = new JoystickButton(_rStick, 2);
    private final Button _gunnerHighButton = new JoystickButton(_rStick, 5);
    
    // gunner buttons //
    private final Button _gunnerFastButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 3));
    private final Button _gunnerSlowButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 4));
    private final Button _gunnerSpinButton = new ConditionalButton(_gunnerModeState, 
    			new JoystickAxisThresholdButton(_xStick, XSTICK_LTRIGGER_AXIS, 0.25, 1)
    		);
    private final Button _gunnerFireButton = new ConditionalButton(_gunnerModeState, 
			new JoystickAxisThresholdButton(_xStick, XSTICK_RTRIGGER_AXIS, 0.25, 1)
		);
    private final Button _gunnerHomeButton   = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 5));
    private final Button _gunnerSuckButton   = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 6));
    private final Button _gunnerDriveButton  = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 1));
    private final Button _gunnerVisionButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 2));
    
    
    // auto test buttons //
    private final Button _autoTestButton1  = new JoystickButton(_rStick, 8);
    
    /*private final JoystickButton _shooterKickButton = new JoystickButton(_lStick, 1);
    private final JoystickButton _shooterOutButton  = new JoystickButton(_lStick, 6);
    private final JoystickButton _shooterInButton   = new JoystickButton(_lStick, 7);
    
    private final JoystickButton _shooterInButton2  = new JoystickButton(_lStick, 8);
    private final JoystickButton _shooterOutButton2 = new JoystickButton(_lStick, 9);
    
    private final JoystickButton _shooterOutButton3 = new JoystickButton(_lStick, 11);
    private final JoystickButton _shooterInButton3  = new JoystickButton(_lStick, 10);
    */
    
    public OI() {
        
        _intakeButton.whileHeld(new IntakeBall());
        _outakeButton.whileHeld(new OutakeBall());
        
        _homePositionButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.Home));
        _loadPositionButton.whenPressed(new LoadBall());
        
        // broken for now //
        //_cameraSpyButton.whileHeld(new SetCameraPosition(CameraPosition.IntakeSight));
        
        _gunnerLowButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.GunnerLow));
        _gunnerMidButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.GunnerMiddle));
        _gunnerHighButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.GunnerHigh));
        
        _gunnerFastButton.whileHeld(new OperateGimbalFast());
        _gunnerSlowButton.whileHeld(new OperateGimbalSlow());
        _gunnerSpinButton.whileHeld(new SetShooterWheelSpeed(5500));
        _gunnerFireButton.whileHeld(new SetShooterKicker(true));
        _gunnerSuckButton.whileHeld(new SetShooterWheelPower(-1.0));
        _gunnerHomeButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.Home));
        
        _gunnerDriveButton.whileHeld(new OperateGunnerDrive());
        _gunnerVisionButton.whileHeld(new SeekGimbalToVision());
        
        
        /*
        _navCalibrateButton.whenPressed(new CalibrateNavigationSensor());
        */
        _autoTestButton1.whenPressed(new AutoDriveStraight(36));
        //_autoTestButton2.whenPressed(new AutoDriveStraight(120));
        //_autoTestButton3.whenPressed(new AutoTurn(90));
        //_autoTestButton4.whenPressed(new AutoGrabTrashCan());
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
    
    public Button getIntakeOperateButton() {
    	return _intakeOperateButton;
    }

    public Button getAuxOperateButton() {
    	return _auxOperateButton;
    }
    
    public boolean getHomeStateEnabled() {
    	return _homeModeState.get();
    }
    
    public void setHomeStateEnabled(boolean enabled) {
    	_homeModeState.setPressed(enabled);
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