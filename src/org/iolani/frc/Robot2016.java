
package org.iolani.frc;

import org.iolani.frc.commands.*;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import org.iolani.frc.commands.*;
//import org.iolani.frc.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot2016 extends IterativeRobot {

    Command _autoCommand;
	PowerDistributionPanel _pdp;
	Compressor             _compressor;
	//SendableChooser 	   _autoChooser;
	Preferences            _prefs;
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	_pdp = new PowerDistributionPanel();
    	_compressor = new Compressor();
    	
    	_pdp.clearStickyFaults();
    	_compressor.clearAllPCMStickyFaults();
    	_compressor.start();
    	
    	/*_autoChooser = new SendableChooser();
    	_autoChooser.addDefault("Do Nothing",  new CommandGroup());
    	_autoChooser.addObject("Trash Can 3", new AutoGrabTrashCan(AutoGrabTrashCan.kLEFT));
    	_autoChooser.addObject("Trash Can 1", new AutoGrabTrashCan(AutoGrabTrashCan.kRIGHT));
    	_autoChooser.addObject("Drive Only", new AutoDriveOnly());*/
    	//SmartDashboard.putData("Autonomous mode chooser", _autoChooser);
    	_prefs = Preferences.getInstance();
    	
        // instantiate the command used for the autonomous period
        //_autoCommand = new AutoGrabTrashCan();
		//_autoCommand = new AutoDriveOnly();
        
    	CommandBase.init();
		new CalibrateNavigationSensor().start();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putData(Scheduler.getInstance());
	}

    public void autonomousInit() {
    	//_autoCommand = (Command) _autoChooser.getSelected();
    	switch(_prefs.getInt("auto-program-number", 0)) {
    		case 0: _autoCommand = null; break;
//    		case 1: _autoCommand = new AutoGrabTrashCan(AutoGrabTrashCan.kLEFT); break;
//   		case 2: _autoCommand = new AutoGrabTrashCan(AutoGrabTrashCan.kRIGHT); break;
//   		case 3: _autoCommand = new AutoDriveOnly(); break;
    		default: _autoCommand = null; break;
    	}
    	if(_autoCommand != null) _autoCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	System.out.println("hello");
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (_autoCommand != null) _autoCommand.cancel();
        
        // home the shooter // 
        new SeekGimbalToPosition(GimbalPosition.Home).start();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        
        CommandBase.intake.debug();
        CommandBase.shooterWheels.debug();
        CommandBase.shooterGimbal.debug();
        CommandBase.drivetrain.debug();
        CommandBase.camera.debug();
        CommandBase.navsensor.debug();
        
        SmartDashboard.putBoolean("home-mode",   CommandBase.oi.getHomeStateEnabled());
        SmartDashboard.putBoolean("gunner-mode", CommandBase.oi.getGunnerControlEnabled());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
