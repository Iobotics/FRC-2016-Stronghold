package org.iolani.frc.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Button class that is pressed when an axis is within one or more ranges. This is used for
 * the trigger "buttons" on the FTC controller which are really one DoF analog axes.
 * 
 * @author jmalins
 */
public class JoystickAxisThresholdButton extends Button {

	private final GenericHID _joystick;
	private final int        _axisNumber;
	private final double     _threshold1Min;
	private final double     _threshold1Max;
	private final double     _threshold2Min;
	private final double     _threshold2Max;
	
	/**
	 * Constructor. Define a button that is considered pressed when the axis value is within a single interval.
	 * 
	 * @param joystick     - the joystick
	 * @param axisNumber   - the axis
	 * @param thresholdMin - the inclusive start of the interval that is considered pressed
	 * @param thresholdMax - the inclusive end of the interval that is considered pressed
	 */
	public JoystickAxisThresholdButton(GenericHID joystick, int axisNumber, double thresholdMin, double thresholdMax) {
		this(joystick, axisNumber, thresholdMin, thresholdMax, 2.0, -2.0); // include a disjoint interval to disable second threshold //
	}
	
	/**
	 * Constructor. Define a button that is considered pressed when the axis value is within either of two intervals. This
	 * variant is designed for the case of two disjoint intervals (such as -1 >= x >= -0.5 && 0.5 <= x <= 1.0).
	 * 
	 * @param joystick      - the joystick
	 * @param axisNumber    - the axis
	 * @param threshold1Min - the inclusive start of the first interval that is considered pressed
	 * @param threshold1Max - the inclusive end of the first interval that is considered pressed
	 * @param threshold2Min - the inclusive start of the second interval that is considered pressed
	 * @param threshold2Max - the inclusive end of the second interval that is considered pressed
	 */
	public JoystickAxisThresholdButton(GenericHID joystick, int axisNumber, double threshold1Min, double threshold1Max, 
			double threshold2Min, double threshold2Max) 
	{
		_joystick   = joystick;
		_axisNumber = axisNumber;
		_threshold1Min = threshold1Min;
		_threshold1Max = threshold1Max;
		_threshold2Min = threshold2Min;
		_threshold2Max = threshold2Max;
	}
	
	@Override
	public boolean get() {
		double value = _joystick.getRawAxis(_axisNumber);
		return ((value >= _threshold1Min) && (value <= _threshold1Max))
			|| ((value >= _threshold2Min) && (value <= _threshold2Max));
	}

}
