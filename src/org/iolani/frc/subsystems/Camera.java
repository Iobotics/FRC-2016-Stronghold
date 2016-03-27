package org.iolani.frc.subsystems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.iolani.frc.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class Camera extends Subsystem {
	
	// control commands //
	public enum CameraCommand {
		Start,
		Stop,
		Restart
	}
	

	public static final double SERVO_MIN = 0.234;
	public static final double SERVO_MAX = 0.525;
	
	private Servo _servo;
	
    public void init() {
    	_servo = new Servo(RobotMap.cameraServoPWM);
    	//this.sendCommand(CameraCommand.Start);
    }
       
    // 0 is fully down, 1 is fully up //
    public double getServoPosition() {
    	return (_servo.get() - SERVO_MIN) / (SERVO_MAX - SERVO_MIN);
    }
    
    // 0 is fully down, 1 is fully up //
    public void setServoPosition(double position) {
    	double value = SERVO_MIN + position * (SERVO_MAX - SERVO_MIN);
    	_servo.set(value);
    }
    
    // FIXME: does not work yet //
    public String sendCommand(CameraCommand cmd) {
    	/*String cmdline = "/etc/init.d/mjpg-streamer " + cmd.toString().toLowerCase();
    	String[] cmdAndArgs = new String[] { "su", "-c", "\"" + cmdline + "\"", "admin" };
    	try {
    		Process p = Runtime.getRuntime().exec(cmdAndArgs);
		
    		StringBuilder out = new StringBuilder();
    		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		PrintWriter stdin = new PrintWriter(new OutputStreamWriter(p.getOutputStream()));
    		String line = null, previous = null;
    		while ((line = br.readLine()) != null) {
    			if(line.startsWith("Password:")) {
    				stdin.println(""); // admin password is blank
    			}
    			if (!line.equals(previous)) {
    				previous = line;
    				out.append(line).append('\n');
    			}
    		}

    		//Check result
    		int res = p.waitFor();
    		System.out.println(out.toString());
    		System.out.println(res);
    		if (res == 0) {
    			return out.toString();
    		}
    		throw new RuntimeException("Camera failed: " + out.toString() + " (" + p.exitValue() + ")");
    	} catch(Exception e) {
    		throw new RuntimeException(e);
    	}*/
    	return "oof";
    }
    
    public void initDefaultCommand() {
    	//this.setDefaultCommand(new SetCameraPosition(CameraPosition.Stowed));
    }
    
    public void debug() {
    	SmartDashboard.putNumber("camera-servo", this.getServoPosition());
    }
}

