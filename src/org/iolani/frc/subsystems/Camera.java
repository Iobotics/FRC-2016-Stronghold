package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;

/**
 *
 */

public class Camera extends Subsystem {

	private static final CameraServer _server = CameraServer.getInstance();
	//private USBCamera _camera;
    
    public void init() {
    	//_camera = new USBCamera(RobotMap.cameraName);
    	_server.setQuality(50);
    	_server.startAutomaticCapture(RobotMap.cameraName);
    }

    public void initDefaultCommand() {
    	
    }
 
    public void debug() {
    	SmartDashboard.putBoolean("camera-started", _server.isAutoCaptureStarted());
    }
}

