
package org.iolani.frc;

//import org.iolani.frc.util.PowerScaler;
import org.iolani.frc.commands.IntakeBall;
import org.iolani.frc.commands.LoadBall;
import org.iolani.frc.commands.OperateGimbalFast;
import org.iolani.frc.commands.OperateGimbalSlow;
import org.iolani.frc.commands.OperateGunnerDrive;
import org.iolani.frc.commands.OutakeBall;
import org.iolani.frc.commands.SeekGimbalToPosition;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.SeekGimbalToVision;
import org.iolani.frc.commands.SetShooterKicker;
import org.iolani.frc.commands.SetShooterWheelPower;
import org.iolani.frc.commands.SetShooterWheelSpeed;
import org.iolani.frc.commands.auto.AutoDriveDeadReckon;
import org.iolani.frc.commands.auto.AutoDriveStraight;
import org.iolani.frc.commands.auto.AutoDriveTimed;
import org.iolani.frc.commands.auto.AutoLowBarAndShoot;
import org.iolani.frc.commands.auto.AutoTurn;
import org.iolani.frc.commands.auto.AutoVisionAzimuth;
import org.iolani.frc.util.ConditionalButton;
import org.iolani.frc.util.JoystickAxisThresholdButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import org.iolani.frc.commands.debug.*;

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
    private final XboxController _xStick = new XboxController(3);
    
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
    private final Button _gunnerSpinButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 6));
    private final Button _gunnerFireButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 5));
    private final Button _gunnerHomeButton   = new ConditionalButton(_gunnerModeState, 
			new JoystickAxisThresholdButton(_xStick, XSTICK_RTRIGGER_AXIS, 0.25, 1)
    	);
    private final Button _gunnerSuckButton   = new ConditionalButton(_gunnerModeState, 
			new JoystickAxisThresholdButton(_xStick, XSTICK_LTRIGGER_AXIS, 0.25, 1)
		);
    private final Button _gunnerDriveButton  = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 1));
    private final Button _gunnerVisionButton = new ConditionalButton(_gunnerModeState, new JoystickButton(_xStick, 2));
    
    
    // auto test buttons //
    private final Button _autoTestButton0  = new JoystickButton(_rStick, 7);
    private final Button _autoTestButton1  = new JoystickButton(_rStick, 8);
    private final Button _autoTestButton2  = new JoystickButton(_rStick, 9);
    private final Button _autoTestButton3  = new JoystickButton(_rStick, 10);
    private final Button _autoTestButton4  = new JoystickButton(_rStick, 11);
    private final Button _autoTestButton5  = new JoystickButton(_rStick, 6);
    
    public OI() {
        
        _intakeButton.whileHeld(new IntakeBall());
        _outakeButton.whileHeld(new OutakeBall());
        
        _homePositionButton.whenPressed(new SeekGimbalToPosition(GimbalPosition.Home));
        _loadPositionButton.whenPressed(new LoadBall());
        
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
        
        _autoTestButton0.whenPressed(new AutoDriveTimed(2.0, AutoDriveDeadReckon.DEFAULT_POWER,  AutoDriveDeadReckon.DEFAULT_POWER * 0.9));
        _autoTestButton1.whenPressed(new AutoDriveDeadReckon(120, AutoDriveDeadReckon.DEFAULT_POWER, AutoDriveDeadReckon.DEFAULT_POWER * 0.9));
        _autoTestButton2.whenPressed(new AutoDriveStraight(150));
        _autoTestButton3.whenPressed(new AutoTurn(90));
        _autoTestButton4.whenPressed(new AutoLowBarAndShoot(false));
        _autoTestButton5.whenPressed(new AutoVisionAzimuth(5));
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
    
    public boolean getGunnerControlEnabled() {
    	return _gunnerModeState.get();
    }
    
    public void setGunnerControlEnabled(boolean enabled) {
    	_gunnerModeState.setPressed(enabled);
    }
    
    public XboxController getGunnerStick() {
    	return _xStick;
    }
}