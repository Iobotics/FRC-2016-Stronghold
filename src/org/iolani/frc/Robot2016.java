
package org.iolani.frc;

import org.iolani.frc.commands.*;
import org.iolani.frc.commands.SeekGimbalToPosition.GimbalPosition;
import org.iolani.frc.commands.auto.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PrintCommand;
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
    	
    	_prefs = Preferences.getInstance();
        
    	CommandBase.init();
		new CalibrateNavigationSensor().start();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit() {
    	SmartDashboard.putData(Scheduler.getInstance());
    }
    
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		
		int autonum = _prefs.getInt("auto-program-number", 0);
    	SmartDashboard.putNumber("auto-num", autonum);
    	
    	this.debugStuff();
	}

    public void autonomousInit() {
    	int autonum = _prefs.getInt("auto-program-number", 0);
    	SmartDashboard.putNumber("auto-num", autonum);
    	// pick auto command via program number //
    	switch(autonum) {
    		case 0: _autoCommand = new PrintCommand("Nihilism: Never Not Nothing"); break;
    		case 1: _autoCommand = new AutoDriveOnly(); break;
    		case 2: _autoCommand = new AutoLowBarAndShoot3(true); break;
    		case 3: _autoCommand = new AutoGimbalAndShoot(200, 47.0, 0.0); break;
    		case 4: _autoCommand = new AutoGimbalAndShoot(230, 53.0, 20.0); break;
    		case 5: _autoCommand = new AutoGimbalAndShoot(230, 53.0, -20.0); break;
    		case 6: _autoCommand = new AutoDelayAndDrive(); break;
    		case 7: _autoCommand = new AutoDriveStraight(40); break;
    		case 9: _autoCommand = new AutoLowBarAndShoot(true); break;
    		default: _autoCommand = null; break;
    	}
    	if(_autoCommand != null) _autoCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
        this.debugStuff();
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
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        
        this.debugStuff();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public void debugStuff() {
    	CommandBase.intake.debug();
        CommandBase.shooterWheels.debug();
        CommandBase.shooterGimbal.debug();
        CommandBase.drivetrain.debug();
        CommandBase.camera.debug();
        CommandBase.navsensor.debug();
        
        SmartDashboard.putBoolean("gunner-mode", CommandBase.oi.getGunnerControlEnabled());
    }
}
