package org.iolani.frc.subsystems;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;

import org.iolani.frc.RobotMap;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class DistanceSensor extends Subsystem {
	
	public static final double SERVO_MIN = 0.234;
	public static final double SERVO_MAX = 0.525;
	
	private static final int BAUD_RATE = 19200;
	
	private Servo  _servo;
	private SerialPortReader _reader;
	
    public void init() {
    	_servo = new Servo(RobotMap.distanceServoPWM);
    	
    	_reader = new SerialPortReader(SerialPort.Port.kMXP, BAUD_RATE, 0.02);
    	_reader.start();
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
    
    public void setServoRaw(double value) {
    	_servo.set(value);
    }
    
    public void initDefaultCommand() {
    	//this.setDefaultCommand(new SetCameraPosition(CameraPosition.Stowed, false));
    	//this.setDefaultCommand(new OperateCameraServo());
    }
    
    public void debug() {
    	SmartDashboard.putNumber("distance-servo", this.getServoPosition());
    	//SmartDashboard.putNumber("camera-servo", _servo.get());
    }
    
    private class SerialPortReader implements Runnable {
  
    	private final double     _delay;
    	private final Thread     _thread;
    	private final SerialPort _port;
    	
    	public SerialPortReader(SerialPort.Port port, int baudRate, double delay) {
    		_thread = new Thread(this);
    		_delay  = delay;
    		
    		_port = new SerialPort(baudRate, port);
    		_port.setReadBufferSize(32);
            _port.setTimeout(0.1);
            _port.enableTermination('\n');
            _port.reset();
    	}
    	
    	public void start() {
    		_thread.start();
    	}
    	
    	public void run() {
    		while(true) {
    			String str = _port.readString();
    			SmartDashboard.putString("distance-serial", "(" + str + ")(" + str.length() + ")");
    			Timer.delay(_delay);
    		}
    	}
    }
}

